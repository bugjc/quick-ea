
package com.bugjc.ea.code.generator.config.builder;

import cn.hutool.core.bean.BeanUtil;
import com.bugjc.ea.code.generator.config.*;
import com.bugjc.ea.code.generator.model.*;
import com.bugjc.ea.code.generator.core.db.querys.H2Query;
import com.bugjc.ea.code.generator.core.db.rules.NamingStrategy;
import com.bugjc.ea.code.generator.core.annotation.DbType;
import com.bugjc.ea.code.generator.core.toolkit.ArrayUtils;
import com.bugjc.ea.code.generator.core.toolkit.StringPool;
import com.bugjc.ea.code.generator.core.toolkit.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 配置汇总 传递给文件生成工具
 *
 * @author YangHu, tangguo, hubin, Juzi
 * @since 2016-08-30
 */
public class ConfigBuilder {

    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private Connection connection;

    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;
    /**
     * 模板配置详情
     */
    private List<TemplateEntity> templateList;

    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 包配置
     */
    private TemplateConfig templateConfig;

    /**
     * 过滤正则
     */
    private static final Pattern REGX = Pattern.compile("[~!/@#$%^&*()-_=+\\\\|[{}];:'\",<.>?]+");

    /**
     * 在构造器中处理配置
     *
     * @param templateConfig   包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param globalConfig     全局配置
     */
    public ConfigBuilder(TemplateConfig templateConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                         GlobalConfig globalConfig) {
        // 全局配置
        this.globalConfig = Optional.ofNullable(globalConfig).orElseGet(GlobalConfig::new);
        //数据源配置
        this.dataSourceConfig = dataSourceConfig;
        handlerDataSource(dataSourceConfig);
        // 策略配置
        this.strategyConfig = Optional.ofNullable(strategyConfig).orElseGet(StrategyConfig::new);
        handlerStrategy(this.strategyConfig);
        // 包配置
        this.templateConfig = templateConfig;
        //处理模板
        handlerTemplate();
    }

    /**
     * 渲染对象 MAP 信息
     *
     * @param tableInfo 表信息对象
     * @return ignore
     */
    private Map<String, Object> getObjectMap(TemplateEntity template, TableInfo tableInfo) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("template", template);
        objectMap.put("author", this.getGlobalConfig().getAuthor());
        //TODO ID Type
        objectMap.put("idType", null);
        objectMap.put("logicDeleteFieldName", this.getStrategyConfig().getLogicDeleteFieldName());
        objectMap.put("versionFieldName", this.getStrategyConfig().getVersionFieldName());
        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        objectMap.put("table", tableInfo);
        objectMap.put("entitySerialVersionUID", this.getStrategyConfig().isEntitySerialVersionUID());
        objectMap.put("entityColumnConstant", this.getStrategyConfig().isEntityColumnConstant());
        objectMap.put("entityBuilderModel", this.getStrategyConfig().isChainModel());
        objectMap.put("chainModel", this.getStrategyConfig().isChainModel());
        objectMap.put("entityLombokModel", this.getStrategyConfig().isEntityLombokModel());
        objectMap.put("entityBooleanColumnRemoveIsPrefix", this.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
        return objectMap;
    }

    // ************************ 曝露方法 BEGIN*****************************


    /**
     * 所有包配置信息
     *
     * @return 包配置
     */
    public List<TemplateEntity> getTemplateList() {
        return templateList;
    }

    /**
     * 表信息
     *
     * @return 所有表信息
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    public ConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }

    // ****************************** 曝露方法 END**********************************

    /**
     * 处理模板配置
     */
    private void handlerTemplate() {
        String outputDir = this.globalConfig.getOutputDir();
        List<TableInfo> tableInfos = getTableInfoList();
        templateList = new ArrayList<>();
        for (TableInfo tableInfo : tableInfos) {
            //处理参数
            List<TemplateEntity> middlePackageInfos = new ArrayList<>();
            for (TemplateEntity template : templateConfig.getTemplates()) {
                TemplateEntity newTemplate = new TemplateEntity();
                BeanUtil.copyProperties(template, newTemplate);

                String path = joinPackage(template.getParent(), template.getPackageName());
                newTemplate.setPackagePath(path);

                String absolutePath = joinPath(outputDir, path);
                newTemplate.setAbsolutePath(absolutePath);

                String fileSuffix = ConstVal.JAVA_SUFFIX;
                if (newTemplate.getTemplatePath().endsWith(ConstVal.XML_FREEMARKER_SUFFIX)) {
                    fileSuffix = ConstVal.XML_SUFFIX;
                }
                String filePath = newTemplate.getAbsolutePath() + File.separator + String.format(newTemplate.getFileNamingConvention(), tableInfo.getClassName()) + fileSuffix;
                newTemplate.setFilePath(filePath);

                String entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategyConfig.getNaming(), strategyConfig.getTablePrefix()));
                String className = String.format(template.getFileNamingConvention(), entityName);
                newTemplate.setClassName(className);

                String referencePath = newTemplate.getPackagePath() + "." + className;
                newTemplate.setReferencePath(referencePath);

                middlePackageInfos.add(newTemplate);
            }

            //处理依赖参数
            List<TemplateEntity> finalTemplateList = new ArrayList<>();
            Map<String, DependClass> dependMap = middlePackageInfos.stream().collect(Collectors.toMap(TemplateEntity::getPackageName, packageInfo -> new DependClass(packageInfo.getPackagePath(), packageInfo.getClassName(), packageInfo.getReferencePath())));
            for (TemplateEntity template : middlePackageInfos) {
                TemplateEntity newTemplate = new TemplateEntity();
                BeanUtil.copyProperties(template, newTemplate);
                //处理外部依赖
                if (template.getDependMap() != null && template.getDependMap().size() > 0) {
                    Map<String, DependClass> dependClasses = new HashMap<>();
                    for (String key : template.getDependMap().keySet()) {
                        String referencePath = template.getDependMap().get(key);
                        String packagePath = referencePath.substring(0, referencePath.lastIndexOf("."));
                        String className = referencePath.substring(packagePath.length() + 1);
                        dependClasses.put(key, new DependClass(packagePath, className, referencePath));
                    }
                    //TODO KEY 冲突检测
                    //合并外部依赖
                    dependMap.putAll(dependClasses);
                }
                //默认全依赖，即当前上下文生成的依赖文件均可应用
                newTemplate.setDependClasses(dependMap);

                //添加模板数据
                newTemplate.setTemplateData(getObjectMap(newTemplate, tableInfo));
                finalTemplateList.add(newTemplate);
            }
            templateList.addAll(finalTemplateList);
        }


    }


    /**
     * 处理数据源配置
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) {
        connection = config.getConn();
    }


    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        tableInfoList = getTablesInfo(config);
    }

    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), config.getNaming(), tablePrefix));
            }

            //增加表类名
            tableInfo.setClassName(entityName);
        }
        return tableList;
    }


    /**
     * 获取所有的数据库表信息
     */
    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        if (config.getNotLikeTable() != null && config.getLikeTable() != null) {
            throw new RuntimeException("<strategy> 标签中 <likeTable> 与 <notLikeTable> 只能配置一项！");
        }
        //所有的表信息
        List<TableInfo> tableList = new ArrayList<>();

        //需要反向生成或排除的表信息
        List<TableInfo> includeTableList = new ArrayList<>();
        List<TableInfo> excludeTableList = new ArrayList<>();

        //不存在的表名
        Set<String> notExistTables = new HashSet<>();
        DbType dbType = this.dataSourceConfig.getDbType();
        IDbQuery dbQuery = this.dataSourceConfig.getDbQuery();
        try {
            String tablesSql = dataSourceConfig.getDbQuery().tablesSql();
            if (DbType.POSTGRE_SQL == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    //pg 默认 schema=public
                    schema = "public";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            } else if (DbType.KINGBASE_ES == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    //kingbase 默认 schema=PUBLIC
                    schema = "PUBLIC";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            } else if (DbType.DB2 == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                if (schema == null) {
                    //db2 默认 schema=current schema
                    schema = "current schema";
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            }
            //oracle数据库表太多，出现最大游标错误
            else if (DbType.ORACLE == dbType) {
                String schema = dataSourceConfig.getSchemaName();
                //oracle 默认 schema=username
                if (schema == null) {
                    schema = dataSourceConfig.getUsername().toUpperCase();
                    dataSourceConfig.setSchemaName(schema);
                }
                tablesSql = String.format(tablesSql, schema);
            }
            StringBuilder sql = new StringBuilder(tablesSql);
            if (config.isEnableSqlFilter()) {
                if (config.getLikeTable() != null) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" LIKE '").append(config.getLikeTable().getValue()).append("'");
                } else if (config.getNotLikeTable() != null) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" NOT LIKE '").append(config.getNotLikeTable().getValue()).append("'");
                }
                if (isInclude) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" IN (")
                            .append(Arrays.stream(config.getInclude()).map(tb -> "'" + tb + "'").collect(Collectors.joining(","))).append(")");
                } else if (isExclude) {
                    sql.append(" AND ").append(dbQuery.tableName()).append(" NOT IN (")
                            .append(Arrays.stream(config.getExclude()).map(tb -> "'" + tb + "'").collect(Collectors.joining(","))).append(")");
                }
            }

            TableInfo tableInfo;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
                 ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    final String tableName = results.getString(dbQuery.tableName());
                    if (StringUtils.isBlank(tableName)) {
                        System.err.println("当前数据库为空！！！");
                        continue;
                    }
                    tableInfo = new TableInfo();
                    tableInfo.setName(tableName);
                    String commentColumn = dbQuery.tableComment();
                    if (StringUtils.isNotBlank(commentColumn)) {
                        String tableComment = results.getString(commentColumn);
                        if (config.isSkipView() && "VIEW".equals(tableComment)) {
                            // 跳过视图
                            continue;
                        }
                        tableInfo.setComment(formatComment(tableComment));
                    }

                    if (isInclude) {
                        for (String includeTable : config.getInclude()) {
                            // 忽略大小写等于 或 正则 true
                            if (tableNameMatches(includeTable, tableName)) {
                                includeTableList.add(tableInfo);
                            } else {
                                //过滤正则表名
                                if (!REGX.matcher(includeTable).find()) {
                                    notExistTables.add(includeTable);
                                }
                            }
                        }
                    } else if (isExclude) {
                        for (String excludeTable : config.getExclude()) {
                            // 忽略大小写等于 或 正则 true
                            if (tableNameMatches(excludeTable, tableName)) {
                                excludeTableList.add(tableInfo);
                            } else {
                                //过滤正则表名
                                if (!REGX.matcher(excludeTable).find()) {
                                    notExistTables.add(excludeTable);
                                }
                            }
                        }
                    }
                    tableList.add(tableInfo);
                }
            }
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }
            if (notExistTables.size() > 0) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }

            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
                includeTableList = tableList;
            }
            if (!isInclude && !isExclude) {
                includeTableList = tableList;
            }
            // 性能优化，只处理需执行表字段 github issues/219
            includeTableList.forEach(ti -> convertTableFields(ti, config));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return processTable(includeTableList, config);
    }


    /**
     * 表名匹配
     *
     * @param setTableName 设置表名
     * @param dbTableName  数据库表单
     * @return ignore
     */
    private boolean tableNameMatches(String setTableName, String dbTableName) {
        return setTableName.equalsIgnoreCase(dbTableName)
                || StringUtils.matches(setTableName, dbTableName);
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableInfo 表信息
     * @param config    命名策略
     */
    private void convertTableFields(TableInfo tableInfo, StrategyConfig config) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();
        DbType dbType = this.dataSourceConfig.getDbType();
        IDbQuery dbQuery = dataSourceConfig.getDbQuery();
        String tableName = tableInfo.getName();
        try {
            String tableFieldsSql = dbQuery.tableFieldsSql();
            Set<String> h2PkColumns = new HashSet<>();
            if (DbType.POSTGRE_SQL == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.KINGBASE_ES == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.OSCAR == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else if (DbType.DB2 == dbType) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaName(), tableName);
            } else if (DbType.ORACLE == dbType) {
                tableName = tableName.toUpperCase();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", dataSourceConfig.getSchemaName()), tableName);
            } else if (DbType.DM == dbType) {
                tableName = tableName.toUpperCase();
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else if (DbType.H2 == dbType) {
                try (PreparedStatement pkQueryStmt = connection.prepareStatement(String.format(H2Query.PK_QUERY_SQL, tableName));
                     ResultSet pkResults = pkQueryStmt.executeQuery()) {
                    while (pkResults.next()) {
                        String primaryKey = pkResults.getString(dbQuery.fieldKey());
                        if (Boolean.parseBoolean(primaryKey)) {
                            h2PkColumns.add(pkResults.getString(dbQuery.fieldName()));
                        }
                    }
                }
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
                    ResultSet results = preparedStatement.executeQuery()) {
                while (results.next()) {
                    TableField field = new TableField();
                    String columnName = results.getString(dbQuery.fieldName());
                    // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                    boolean isId;
                    if (DbType.H2 == dbType) {
                        isId = h2PkColumns.contains(columnName);
                    } else {
                        String key = results.getString(dbQuery.fieldKey());
                        if (DbType.DB2 == dbType || DbType.SQLITE == dbType) {
                            isId = StringUtils.isNotBlank(key) && "1".equals(key);
                        } else {
                            isId = StringUtils.isNotBlank(key) && "PRI".equals(key.toUpperCase());
                        }
                    }

                    // 处理ID
                    if (isId && !haveId) {
                        haveId = true;
                        field.setKeyFlag(true);
                        tableInfo.setHavePrimaryKey(true);
                        field.setKeyIdentityFlag(dbQuery.isKeyIdentity(results));
                    } else {
                        field.setKeyFlag(false);
                    }
                    // 自定义字段查询
                    String[] fcs = dbQuery.fieldCustom();
                    if (null != fcs) {
                        Map<String, Object> customMap = new HashMap<>();
                        for (String fc : fcs) {
                            customMap.put(fc, results.getObject(fc));
                        }
                        field.setCustomMap(customMap);
                    }
                    // 处理其它信息
                    field.setName(columnName);
                    String newColumnName = columnName;
                    IKeyWordsHandler keyWordsHandler = dataSourceConfig.getKeyWordsHandler();
                    if (keyWordsHandler != null) {
                        if (keyWordsHandler.isKeyWords(columnName)) {
                            System.err.println(String.format("当前表[%s]存在字段[%s]为数据库关键字或保留字!", tableName, columnName));
                            field.setKeyWords(true);
                            newColumnName = keyWordsHandler.formatColumn(columnName);
                        }
                    }
                    field.setColumnName(newColumnName);
                    field.setType(results.getString(dbQuery.fieldType()));
                    INameConvert nameConvert = strategyConfig.getNameConvert();
                    if (null != nameConvert) {
                        field.setPropertyName(nameConvert.propertyNameConvert(field));
                    } else {
                        field.setPropertyName(strategyConfig, processName(field.getName(), config.getColumnNaming()));
                    }
                    field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(globalConfig, field));
                    String fieldCommentColumn = dbQuery.fieldComment();
                    if (StringUtils.isNotBlank(fieldCommentColumn)) {
                        field.setComment(formatComment(results.getString(fieldCommentColumn)));
                    }
                    // 填充逻辑判断
                    List<TableFill> tableFillList = getStrategyConfig().getTableFillList();
                    if (null != tableFillList) {
                        // 忽略大写字段问题
                        tableFillList.stream().filter(tf -> tf.getFieldName().equalsIgnoreCase(field.getName()))
                                .findFirst().ifPresent(tf -> field.setFill(tf.getFieldFill().name()));
                    }
                    if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                        // 跳过公共字段
                        commonFieldList.add(field);
                        continue;
                    }
                    fieldList.add(field);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
    }


    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }


    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isBlank(parent) ? subPackage : (parent + StringPool.DOT + subPackage);
    }


    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, strategyConfig.getFieldPrefix());
    }


    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        String propertyName;
        if (ArrayUtils.isNotEmpty(prefix)) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }


    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }


    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }


    /**
     * 格式化数据库注释内容
     *
     * @param comment 注释
     * @return 注释
     * @since 3.4.0
     */
    private String formatComment(String comment) {
        return StringUtils.isBlank(comment) ? StringPool.EMPTY : comment.replaceAll("\r\n", "\t");
    }

}

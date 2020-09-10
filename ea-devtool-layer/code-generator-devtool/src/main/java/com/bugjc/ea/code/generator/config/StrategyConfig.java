
package com.bugjc.ea.code.generator.config;


import com.bugjc.ea.code.generator.core.db.rules.NamingStrategy;
import com.bugjc.ea.code.generator.core.toolkit.StringUtils;
import com.bugjc.ea.code.generator.model.LikeTable;
import com.bugjc.ea.code.generator.model.TableFill;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo, hubin
 * @since 2016/8/30
 */
@Data
@Accessors(chain = true)
public class StrategyConfig {
    /**
     * 是否大写命名
     */
    private boolean isCapitalMode = false;
    /**
     * 是否跳过视图
     */
    private boolean skipView = false;
    /**
     * 名称转换
     */
    private INameConvert nameConvert;
    /**
     * 数据库表映射到实体的命名策略
     */
    private NamingStrategy naming = NamingStrategy.no_change;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    private NamingStrategy columnNaming = null;
    /**
     * 表前缀
     */
    @Setter(AccessLevel.NONE)
    private String[] tablePrefix;
    /**
     * 字段前缀
     */
    @Setter(AccessLevel.NONE)
    private String[] fieldPrefix;
    /**
     * 自定义继承的Entity类全称，带包名
     */
    @Setter(AccessLevel.NONE)
    private String superEntityClass;
    /**
     * 自定义基础的Entity类，公共字段
     */
    @Setter(AccessLevel.NONE)
    private String[] superEntityColumns;
    /**
     * 自定义继承的Mapper类全称，带包名
     */
    //private String superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
    /**
     * 自定义继承的Service类全称，带包名
     */
    private String superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    private String superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superControllerClass;
    /**
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    @Setter(AccessLevel.NONE)
    private String[] include = null;
    /**
     * 需要排除的表名，允许正则表达式<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    @Setter(AccessLevel.NONE)
    private String[] exclude = null;
    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean entitySerialVersionUID = true;
    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private boolean entityColumnConstant = false;
    /**
     * 【实体】是否为构建者模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     *
     * @deprecated 3.3.2 {@link #chainModel}
     */
    @Deprecated
    private boolean entityBuilderModel = false;
    
    /**
     * 【实体】是否为链式模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     *
     * @since 3.3.2
     */
    private boolean chainModel = false;
    
    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private boolean entityLombokModel = false;
    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean entityBooleanColumnRemoveIsPrefix = false;

    /**
     * 驼峰转连字符
     * <pre>
     *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
     * </pre>
     */
    private boolean controllerMappingHyphenStyle = false;
    /**
     * 是否生成实体时，生成字段注解
     */
    private boolean entityTableFieldAnnotationEnable = false;
    /**
     * 乐观锁属性名称
     */
    private String versionFieldName;
    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName;
    /**
     * 表填充字段
     */
    private List<TableFill> tableFillList = null;
    /**
     * 启用sql过滤，语法不能支持使用sql过滤表的话，可以考虑关闭此开关.
     *
     * @since 3.3.1
     */
    private boolean enableSqlFilter = true;
    /**
     * 包含表名
     *
     * @since 3.3.0
     */
    private LikeTable likeTable;
    /**
     * 不包含表名
     *
     * @since 3.3.0
     */
    private LikeTable notLikeTable;

    /**
     * 大写命名、字段符合大写字母数字下划线命名
     *
     * @param word 待判断字符串
     */
    public boolean isCapitalModeNaming(String word) {
        return isCapitalMode && StringUtils.isCapitalMode(word);
    }
    
    /**
     * 表名称包含指定前缀
     *
     * @param tableName 表名称
     * @deprecated 3.3.2 {@link #startsWithTablePrefix(String)}
     */
    @Deprecated
    public boolean containsTablePrefix(String tableName) {
        if (null != tableName) {
            String[] tps = getTablePrefix();
            if (null != tps) {
                return Arrays.stream(tps).anyMatch(tableName::contains);
            }
        }
        return false;
    }
    
    /**
     * 表名称匹配表前缀
     *
     * @param tableName 表名称
     * @since 3.3.2
     */
    public boolean startsWithTablePrefix(String tableName) {
        if (null != tableName) {
            String[] tps = getTablePrefix();
            if (null != tps) {
                return Arrays.stream(tps).anyMatch(tableName::startsWith);
            }
        }
        return false;
    }
    
    public NamingStrategy getColumnNaming() {
        // 未指定以 naming 策略为准
        return Optional.ofNullable(columnNaming).orElse(naming);
    }

    public StrategyConfig setTablePrefix(String... tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public boolean includeSuperEntityColumns(String fieldName) {
        if (null != superEntityColumns) {
            // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
            return Arrays.stream(superEntityColumns).anyMatch(e -> e.equalsIgnoreCase(fieldName));
        }
        return false;
    }

    public StrategyConfig setSuperEntityColumns(String... superEntityColumns) {
        this.superEntityColumns = superEntityColumns;
        return this;
    }

    public StrategyConfig setInclude(String... include) {
        this.include = include;
        return this;
    }

    public StrategyConfig setExclude(String... exclude) {
        this.exclude = exclude;
        return this;
    }

    public StrategyConfig setFieldPrefix(String... fieldPrefixs) {
        this.fieldPrefix = fieldPrefixs;
        return this;
    }

    public StrategyConfig setSuperServiceClass(Class<?> clazz) {
        this.superServiceClass = clazz.getName();
        return this;
    }

    public StrategyConfig setSuperServiceClass(String superServiceClass) {
        this.superServiceClass = superServiceClass;
        return this;
    }
    
    public StrategyConfig setSuperServiceImplClass(Class<?> clazz) {
        this.superServiceImplClass = clazz.getName();
        return this;
    }
    
    public StrategyConfig setSuperServiceImplClass(String superServiceImplClass) {
        this.superServiceImplClass = superServiceImplClass;
        return this;
    }
    
    public StrategyConfig setSuperControllerClass(Class<?> clazz) {
        this.superControllerClass = clazz.getName();
        return this;
    }

    public StrategyConfig setSuperControllerClass(String superControllerClass) {
        this.superControllerClass = superControllerClass;
        return this;
    }

    /**
     * 是否为构建者模型
     *
     * @return 是否为构建者模型
     * @deprecated 3.3.2 {@link #isChainModel()}
     */
    @Deprecated
    public boolean isEntityBuilderModel() {
        return isChainModel();
    }
    
    /**
     * 设置是否为构建者模型
     *
     * @param entityBuilderModel 是否为构建者模型
     * @return this
     * @deprecated 3.3.2 {@link #setChainModel(boolean)}
     */
    @Deprecated
    public StrategyConfig setEntityBuilderModel(boolean entityBuilderModel) {
        return setChainModel(entityBuilderModel);
    }
    
}

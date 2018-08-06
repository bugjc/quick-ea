package generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import generator.config.GlobalConfiguration;
import generator.constant.GeneratorKeyConstant;
import generator.constant.ProjectConstant;
import generator.service.IGenerator;
import generator.util.GeneratorUtil;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成服务实现
 * @author aoki
 */
@Service
public class GeneratorService implements IGenerator {

    @Override
    public void genCode(GlobalConfiguration globalConfiguration, GeneratedKey generatedKey, String type, String tableName, String  modelName) {
        this.genModelAndMapper(tableName, modelName,generatedKey,type,globalConfiguration);
        this.genService(tableName, modelName,globalConfiguration);
        this.genController(tableName, modelName,globalConfiguration);
    }

    @Override
    public void genModelAndMapper(String tableName, String modelName, GeneratedKey generatedKey,String type, GlobalConfiguration globalConfiguration) {
        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        if (type.equals(GeneratorKeyConstant.key.MySql.name())){
            jdbcConnectionConfiguration.setConnectionURL(globalConfiguration.mysqlUrl);
            jdbcConnectionConfiguration.setUserId(globalConfiguration.mysqlUsername);
            jdbcConnectionConfiguration.setPassword(globalConfiguration.mysqlPassword);
            jdbcConnectionConfiguration.setDriverClass(globalConfiguration.mysqlDriverClassName);
        }else if (type.equals(GeneratorKeyConstant.key.SqlServer.name())){
            jdbcConnectionConfiguration.setConnectionURL(globalConfiguration.mssqlUrl);
            jdbcConnectionConfiguration.setUserId(globalConfiguration.mssqlUsername);
            jdbcConnectionConfiguration.setPassword(globalConfiguration.mssqlPassword);
            jdbcConnectionConfiguration.setDriverClass(globalConfiguration.mssqlDriverClassName);
        }

        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("mappers", ProjectConstant.MAPPER_INTERFACE_REFERENCE);
        context.addPluginConfiguration(pluginConfiguration);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(globalConfiguration.projectPath + globalConfiguration.javaPath);
        javaModelGeneratorConfiguration.setTargetPackage(ProjectConstant.MODEL_PACKAGE);
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(globalConfiguration.projectPath + globalConfiguration.resourcePath);
        sqlMapGeneratorConfiguration.setTargetPackage("mapper");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(globalConfiguration.projectPath + globalConfiguration.javaPath);
        javaClientGeneratorConfiguration.setTargetPackage(ProjectConstant.MAPPER_PACKAGE);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        if (StrUtil.isNotEmpty(modelName)){
            tableConfiguration.setDomainObjectName(modelName);
        }
        tableConfiguration.setGeneratedKey(generatedKey);
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<String>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StrUtil.isEmpty(modelName)){
            modelName = GeneratorUtil.tableNameConvertUpperCamel(tableName);
        }
        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "Mapper.java 生成成功");
        System.out.println(modelName + "Mapper.xml 生成成功");
    }

    @Override
    public void genService(String tableName, String modelName, GlobalConfiguration globalConfiguration) {
        try {
            freemarker.template.Configuration cfg = GeneratorUtil.getConfiguration(globalConfiguration);

            Map<String, Object> data = new HashMap<>(12);
            data.put("date", globalConfiguration.date);
            data.put("author", globalConfiguration.author);
            String modelNameUpperCamel = StrUtil.isEmpty(modelName) ? GeneratorUtil.tableNameConvertUpperCamel(tableName) : modelName;
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", GeneratorUtil.tableNameConvertLowerCamel(tableName));
            data.put("basePackage", ProjectConstant.BASE_PACKAGE);

            File file = new File(globalConfiguration.projectPath + globalConfiguration.javaPath + GeneratorUtil.PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data,
                    new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(globalConfiguration.projectPath + globalConfiguration.javaPath + GeneratorUtil.PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data,
                    new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    @Override
    public void genController(String tableName, String modelName, GlobalConfiguration globalConfiguration) {
        try {
            freemarker.template.Configuration cfg = GeneratorUtil.getConfiguration(globalConfiguration);

            Map<String, Object> data = new HashMap<>(12);
            data.put("date", globalConfiguration.date);
            data.put("author", globalConfiguration.author);
            String modelNameUpperCamel = StrUtil.isEmpty(modelName) ? GeneratorUtil.tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", GeneratorUtil.modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", ProjectConstant.BASE_PACKAGE);

            File file = new File(globalConfiguration.projectPath + globalConfiguration.javaPath + GeneratorUtil.PACKAGE_PATH_CONTROLLER + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }
}

package generator.util;

import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import generator.config.GlobalConfiguration;
import generator.constant.ProjectConstant;

import java.io.File;
import java.io.IOException;

/**
 * 代码生成工具
 * @author aoki
 */
public class GeneratorUtil {
    /**
     * 生成的Service存放路径
     */
    public static final String PACKAGE_PATH_SERVICE = packageConvertPath(ProjectConstant.SERVICE_PACKAGE);
    /**
     * 生成的Service实现存放路径
     */
    public static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(ProjectConstant.SERVICE_IMPL_PACKAGE);
    /**
     * 生成的Controller存放路径
     */
    public static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(ProjectConstant.CONTROLLER_PACKAGE);


    public static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    public static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
    }

    public static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    public static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    public static String tableNameConvertMappingPath(String tableName) {
        //兼容使用大写的表名
        tableName = tableName.toLowerCase();
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    public static freemarker.template.Configuration getConfiguration(GlobalConfiguration globalConfiguration) throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(globalConfiguration.templatePath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
}

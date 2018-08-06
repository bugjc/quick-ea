package generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 代码生成全局配置
 * @author aoki
 */
@Configuration
public class GlobalConfiguration {

    /**
     * mysql数据库属性
     */
    @Value("${mysql.jdbc.url}")
    public String mysqlUrl;
    @Value("${mysql.jdbc.username}")
    public String mysqlUsername;
    @Value("${mysql.jdbc.password}")
    public String mysqlPassword;
    @Value("${mysql.jdbc.driver-class-name}")
    public String mysqlDriverClassName;

    /**
     * mssql数据库属性
     */
    @Value("${mssql.jdbc.url}")
    public String mssqlUrl;
    @Value("${mssql.jdbc.username}")
    public String mssqlUsername;
    @Value("${mssql.jdbc.password}")
    public String mssqlPassword;
    @Value("${mssql.jdbc.driver-class-name}")
    public String mssqlDriverClassName;

    /**
     * 生成模板属性
     */
    public String projectPath = System.getProperty("user.dir");
    public String templatePath = projectPath + "/src/main/resources/generator/template";
    /**
     * java文件路径
     */
    @Value("${gen.java-path}")
    public String javaPath;
    /**
     * 资源文件路径
     */
    @Value("${gen.resource-path}")
    public String resourcePath;

    /**
     * 开发者注释信息
     */
    @Value("${gen.author}")
    public String author;
    public String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());


}

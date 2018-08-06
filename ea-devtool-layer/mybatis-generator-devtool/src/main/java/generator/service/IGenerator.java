package generator.service;

import generator.config.GlobalConfiguration;
import org.mybatis.generator.config.GeneratedKey;

/**
 * 代码生成接口
 * @author aoki
 */
public interface IGenerator {

    /**
     * 生成代码
     * @param globalConfiguration
     * @param generatedKey
     * @param type
     * @param tableName
     * @param modelName
     */
    void genCode(GlobalConfiguration globalConfiguration, GeneratedKey generatedKey, String type, String tableName, String modelName);

    /**
     * 生成mapper
     * @param tableName
     * @param modelName
     * @param generatedKey
     * @param type
     * @param globalConfiguration
     */
    void genModelAndMapper(String tableName, String modelName, GeneratedKey generatedKey, String type, GlobalConfiguration globalConfiguration);

    /**
     * 生成service
     * @param tableName
     * @param modelName
     * @param globalConfiguration
     */
    void genService(String tableName, String modelName, GlobalConfiguration globalConfiguration);

    /**
     * 生成controller
     * @param tableName
     * @param modelName
     * @param globalConfiguration
     */
    void genController(String tableName, String modelName, GlobalConfiguration globalConfiguration);
}

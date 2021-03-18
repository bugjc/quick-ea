package com.bugjc.ea.code.generator.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 模板配置
 * 打包当前生成类及依赖类的上下文信息
 *
 * @author aoki
 * @date 2020/8/27
 **/
@Data
public class TemplateEntity implements Serializable {

    /**
     * 父包的路徑（必须）
     * 示例：com.bugjc.ea.devtool.mybatis.code.generator.test
     */
    private String parent;

    /**
     * 包名 （必须）
     * 示例：api
     */
    private String packageName;

    /**
     * 模板文件所在的相对路径 （必须）
     * 示例：/templates/api/Api.java.ftl
     */
    private String templatePath;

    /**
     * 文件命名规约（必须）
     * 示例：%sEntity
     */
    private String fileNamingConvention;

    /**
     * 包的路径
     * 例如：com.bugjc.ea.devtool.mybatis.code.generator.test.api
     */
    private String packagePath;

    /**
     * 包的绝对路径
     * 示例：E://xxx/mybatis-plus-generator-devtool/src/main/java/com/bugjc/ea/devtool/mybatis/code/generator/test/api
     */
    private String absolutePath;

    /**
     * 文件所在的绝对路径
     * 示例：E://xxx/mybatis-plus-generator-devtool/src/main/java/com/bugjc/ea/devtool/mybatis/code/generator/test/api/ArticleApi
     */
    private String filePath;

    /**
     * 模板数据
     */
    private Map<String, Object> templateData;

    /**
     * 外部依赖，有以下几种配置方式：
     * 1. key1 = com.bugjc.code.Test 方式;
     * 2. key2 = com.bugjc.code.* 方式；
     * 会转换成依赖的 {@link DependClass} ,模板中通过 ${package.dependClasses["key1"].className!} 获取依赖对象值;
     */
    private Map<String, String> dependMap;

    /**
     * 外部依赖类（选填）
     * 示例：com.alibaba.fastjson.JSON,org.junit.jupiter.api.Test
     */
    private Map<String, DependClass> externalDependClasses;

    /**
     * 依赖 class，由内部同一上下文的类和外部配置的类组成
     */
    private Map<String, DependClass> dependClasses;

    /**
     * 类名
     * 示例：ArticleApi
     */
    private String className;

    /**
     * 引用路径
     * 示例：com.bugjc.ea.devtool.mybatis.code.generator.test.api.ArticleApi
     */
    private String referencePath;

}

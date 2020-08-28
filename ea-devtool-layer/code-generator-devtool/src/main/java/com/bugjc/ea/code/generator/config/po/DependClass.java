package com.bugjc.ea.code.generator.config.po;

import lombok.Data;

import java.io.Serializable;

/**
 * 依赖的 Class
 * @author aoki
 * @date 2020/8/28
 * **/
@Data
public class DependClass implements Serializable {

    /**
     * 包的路径
     */
    private String packagePath;

    /**
     * 类名
     */
    private String className;

    /**
     * 引用路径
     */
    private String referencePath;

    /**
     * 初始化依赖类
     *
     * @param packagePath   --包的路径
     * @param className     --类名
     * @param referencePath --引用路径
     */
    public DependClass(String packagePath, String className, String referencePath) {
        this.packagePath = packagePath;
        this.className = className;
        this.referencePath = referencePath;
    }
}

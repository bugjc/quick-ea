package com.bugjc.ea.opensdk.mock.data.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 测试类
 * @author aoki
 * @date 2020/9/25
 * **/
@Data
public class Param implements Serializable {

    /**
     * 基本数据类型
     */
    private Byte aByte;
    private Short bShort;
    private Integer cInt;
    private Long dLong;
    private Float eFloat;
    private Double fDouble;
    private Character gChar;
    private Boolean hBoolean;
    private String iString;
    private String iStringChild;

    /**
     * 数组
     */
    private String[] array;

    /**
     * map 基本对象
     */
    private Map<String, String> map;

    /**
     * map 实体对象
     * 结果示例：{"key1":{"field1":"1","field2":["1.1","1.2"]},"key2":{"field1":"2","field2":["2.1","2.2"]}}
     */
    private Map<String, Entity> map1;

    /**
     * List<Entity>
     */
    private List<Entity> entities;

    /**
     * List<String>
     */
    private List<String> stringList;

    /**
     * List<Integer>
     */
    private List<Integer> integerList;


    /**
     * 嵌套 map 对象
     */
    private Map<String, Map<String, String>> map2;

    /**
     * 嵌套 map 对象
     */
    private Map<String, Map<String, Entity>> map3;

    /**
     * map 嵌套 list entity
     */
    private Map<String, List<Entity>> map4;

    /**
     * map 嵌套 list basic
     */
    private Map<String, List<String>> map5;

    /**
     * map 嵌套 list basic
     */
    private Map<String, List<List<String>>> map6;

    /**
     * map 嵌套 list Entity
     */
    private Map<String, List<List<Entity>>> map7;
}
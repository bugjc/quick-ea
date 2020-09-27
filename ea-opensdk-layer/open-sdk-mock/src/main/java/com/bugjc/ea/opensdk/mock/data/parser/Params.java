package com.bugjc.ea.opensdk.mock.data.parser;

import cn.hutool.core.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据分组
 *
 * @author aoki
 * @date 2020/8/12
 **/
public class Params {

    /**
     * 上级分组容器
     */
    @Getter
    private final GroupContainer upperGroupContainer;

    /**
     * 当前处理的字段
     */
    @Getter
    @Setter
    private NewField currentField;

    /**
     * 要处理的字段列表
     */
    @Getter
    private final List<NewField> fields;

    /**
     * 初始化参数对象
     *
     * @param upperGroupContainer
     * @param fields
     */
    public Params(GroupContainer upperGroupContainer, List<NewField> fields) {
        this.upperGroupContainer = upperGroupContainer;
        this.fields = fields;
    }

    /**
     * 创建新的参数对象
     *
     * @param groupContainer
     * @param fields
     * @return
     */
    public static Params create(GroupContainer groupContainer, List<NewField> fields) {
        return new Params(groupContainer, fields);
    }

    /**
     * 获取要处理的 entity 字段列表
     *
     * @param valueType
     * @return
     */
    public List<NewField> getEntityFields(Type valueType) {
        Class<?> entityClass = (Class<?>) valueType;
        return Arrays.stream(entityClass.getDeclaredFields())
                .map(fieldMap -> new NewField(fieldMap.getName(), fieldMap.getType(), fieldMap.getGenericType()))
                .collect(Collectors.toList());
    }


    /**
     * 获取要处理的基础类型字段列表
     * @param type
     * @param valueType
     * @param containerType
     * @return
     */
    public List<NewField> getFields(Class<?> type, Type valueType, ContainerType containerType) {
        List<NewField> valueFields = new ArrayList<>();
        for (int i = 0; i < RandomUtil.randomInt(1, 10); i++) {
            String name = String.valueOf(i);
            NewField newField = new NewField(name, type, valueType, containerType);
            valueFields.add(newField);
        }
        return valueFields;
    }
}

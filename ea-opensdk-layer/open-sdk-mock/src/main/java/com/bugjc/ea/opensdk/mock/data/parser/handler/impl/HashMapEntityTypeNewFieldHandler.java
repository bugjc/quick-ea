package com.bugjc.ea.opensdk.mock.data.parser.handler.impl;

import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.opensdk.mock.data.parser.*;
import com.bugjc.ea.opensdk.mock.data.parser.handler.NewFieldHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.bugjc.ea.opensdk.mock.data.parser.PropertyParser.deconstruction;

/**
 * HashMap Entity 字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public class HashMapEntityTypeNewFieldHandler implements NewFieldHandler {

    public final static HashMapEntityTypeNewFieldHandler INSTANCE = new HashMapEntityTypeNewFieldHandler();

    @Override
    public void process(Params input, Container output) {
        //HashMap_Entity 类型的字段递归解构非字段部分，如：属性配置 com.bugjc.map.key.entity.field1 中的 key 部分
        ParameterizedType parameterizedType = (ParameterizedType) input.getCurrentField().getGenericType();
        //获取 Map<key,value> 类型
        Class<?> keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        Type valueType = parameterizedType.getActualTypeArguments()[1];

        ContainerType virtualType = ContainerType.Virtual_HashMap_Entity;
        if (TypeUtil.isList(valueType)) {
            virtualType = ContainerType.ArrayList_Entity;
        }

        List<NewField> valueFields = new ArrayList<>();
        for (int i = 0; i < RandomUtil.randomInt(1, 10); i++) {
            String name = String.valueOf(i);
            NewField newField = new NewField(name, keyType, valueType, virtualType);
            valueFields.add(newField);
        }

        GroupContainer nextGroupContainer = GroupContainer.create(
                output.getCurrentGroupContainer().getCurrentContainerType(),
                output.getCurrentGroupContainer().getCurrentGroupName(),
                ContainerType.HashMap_Entity);

        Params newInput = Params.create(nextGroupContainer, valueFields);
        deconstruction(newInput, output);
    }
}

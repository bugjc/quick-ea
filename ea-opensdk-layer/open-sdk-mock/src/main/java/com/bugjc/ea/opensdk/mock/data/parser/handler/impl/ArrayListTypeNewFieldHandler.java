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
 * List 字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public class ArrayListTypeNewFieldHandler implements NewFieldHandler {

    public final static ArrayListTypeNewFieldHandler INSTANCE = new ArrayListTypeNewFieldHandler();

    @Override
    public void process(Params input, Container output) {

        NewField field = input.getCurrentField();
        ContainerType currentContainerType = output.getCurrentGroupContainer().getCurrentContainerType();
        String currentGroupName = output.getCurrentGroupContainer().getCurrentGroupName();

        ParameterizedType parameterizedType = (ParameterizedType) input.getCurrentField().getGenericType();
        Type valueType = parameterizedType.getActualTypeArguments()[0];

        ContainerType containerType;
        if (TypeUtil.isList(valueType)) {
            containerType = ContainerType.ArrayList;
        } else if (TypeUtil.isMap(valueType)) {
            containerType = ContainerType.HashMap;
        } else if (TypeUtil.isBasic(valueType)) {
            VirtualArrayListTypeNewFieldHandler.INSTANCE.process(input, output);
            return;
        } else {
            throw new NullPointerException();
        }

        GroupContainer nextGroupContainer = GroupContainer.create(currentContainerType, currentGroupName, ContainerType.ArrayList);
        Params newInput = Params.create(
                nextGroupContainer,
                input.getFields(field.getType(), valueType, containerType));
        deconstruction(newInput, output);
    }
}

package com.bugjc.ea.opensdk.mock.data.parser.handler.impl;

import com.bugjc.ea.opensdk.mock.data.parser.*;
import com.bugjc.ea.opensdk.mock.data.parser.handler.NewFieldHandler;

import java.lang.reflect.Type;

import static com.bugjc.ea.opensdk.mock.data.parser.PropertyParser.deconstruction;

/**
 * 基础字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public class VirtualHashMapEntityTypeNewFieldHandler implements NewFieldHandler {

    public final static VirtualHashMapEntityTypeNewFieldHandler INSTANCE = new VirtualHashMapEntityTypeNewFieldHandler();

    @Override
    public void process(Params input, Container output) {

        Type valueType = input.getCurrentField().getGenericType();
        if (!TypeUtil.isJavaBean(valueType)) {
            throw new NullPointerException();
        }

        ContainerType currentContainerType = output.getCurrentGroupContainer().getCurrentContainerType();
        ContainerType upperContainerType = output.getCurrentGroupContainer().getUpperContainerType();
        String currentGroupName = output.getCurrentGroupContainer().getCurrentGroupName();

        GroupContainer nextGroupContainer = GroupContainer.create(currentContainerType, currentGroupName, upperContainerType);
        Params newInput = Params.create(nextGroupContainer, input.getEntityFields(valueType));
        deconstruction(newInput, output);
    }
}

package com.bugjc.ea.opensdk.mock.data.parser.handler.impl;

import com.bugjc.ea.opensdk.mock.data.parser.Container;
import com.bugjc.ea.opensdk.mock.data.parser.Params;
import com.bugjc.ea.opensdk.mock.data.parser.TypeUtil;
import com.bugjc.ea.opensdk.mock.data.parser.handler.NewFieldHandler;

import java.lang.reflect.Type;

/**
 * Virtual HashMap 字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public class VirtualHashMapTypeNewFieldHandler implements NewFieldHandler {

    public final static VirtualHashMapTypeNewFieldHandler INSTANCE = new VirtualHashMapTypeNewFieldHandler();

    @Override
    public void process(Params input, Container output) {
        //判断类型是否存在嵌套
        Type valueType = input.getCurrentField().getGenericType();
        if (!TypeUtil.isBasic(valueType)) {
            throw new NullPointerException();
        }
        BasicTypeNewFieldHandler.INSTANCE.process(input, output);
    }
}

package com.bugjc.ea.opensdk.mock.data.parser.handler.impl;

import cn.hutool.core.util.RandomUtil;
import com.bugjc.ea.opensdk.mock.data.parser.*;
import com.bugjc.ea.opensdk.mock.data.parser.handler.NewFieldHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.bugjc.ea.opensdk.mock.data.parser.PropertyParser.deconstruction;

/*
 * List<Entity> 字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public class ArrayListEntityTypeNewFieldHandler implements NewFieldHandler {

    public final static ArrayListEntityTypeNewFieldHandler INSTANCE = new ArrayListEntityTypeNewFieldHandler();

    @Override
    public void process(Params input, Container output) {
        //ArrayList_Entity 类型的字段递归解构非字段部分，如：属性配置 com.bugjc.list.[0].field1 中的 [0] 部分
        NewField field = input.getCurrentField();
        ContainerType currentContainerType = output.getCurrentGroupContainer().getCurrentContainerType();
        String currentGroupName = output.getCurrentGroupContainer().getCurrentGroupName();

        ParameterizedType parameterizedType = (ParameterizedType) input.getCurrentField().getGenericType();
        Type valueType = parameterizedType.getActualTypeArguments()[0];

        if (TypeUtil.isList(valueType)) {
            List<NewField> valueFields = new ArrayList<>();
            for (int i = 0; i < RandomUtil.randomInt(1, 10); i++) {
                String name = String.valueOf(i);
                NewField newField = new NewField(name, field.getType(), valueType, ContainerType.ArrayList_Entity);
                valueFields.add(newField);
            }

            GroupContainer nextGroupContainer = GroupContainer.create(currentContainerType, currentGroupName, ContainerType.ArrayList_Entity);
            Params newInput = Params.create(nextGroupContainer, valueFields);
            deconstruction(newInput, output);
        } else if (TypeUtil.isMap(valueType)) {
            throw new NullPointerException("TODO");
        }
    }
}

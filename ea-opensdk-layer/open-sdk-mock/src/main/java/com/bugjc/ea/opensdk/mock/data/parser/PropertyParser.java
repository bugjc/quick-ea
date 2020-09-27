package com.bugjc.ea.opensdk.mock.data.parser;

import com.bugjc.ea.opensdk.mock.data.parser.handler.NewFieldHandler;
import com.bugjc.ea.opensdk.mock.data.parser.handler.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 属性解析器
 *
 * @author aoki
 * @date 2020/8/12
 **/
public class PropertyParser {

    /**
     * 注册类型处理器
     */
    private static Map<ContainerType, NewFieldHandler> REGISTER_TYPE_HANDLER = new HashMap<ContainerType, NewFieldHandler>() {{
        put(ContainerType.Basic, BasicTypeNewFieldHandler.INSTANCE);
        put(ContainerType.ArrayList, ArrayListTypeNewFieldHandler.INSTANCE);
        put(ContainerType.Virtual_ArrayList, VirtualArrayListTypeNewFieldHandler.INSTANCE);
        put(ContainerType.ArrayList_Entity, ArrayListEntityTypeNewFieldHandler.INSTANCE);
        put(ContainerType.Virtual_ArrayList_Entity, VirtualArrayListEntityTypeNewFieldHandler.INSTANCE);
        put(ContainerType.HashMap_Entity, HashMapEntityTypeNewFieldHandler.INSTANCE);
        put(ContainerType.Virtual_HashMap_Entity, VirtualHashMapEntityTypeNewFieldHandler.INSTANCE);
        put(ContainerType.HashMap, HashMapTypeNewFieldHandler.INSTANCE);
        put(ContainerType.Virtual_HashMap, VirtualHashMapTypeNewFieldHandler.INSTANCE);
    }};

    /**
     * 解构对象字段数据
     *
     * @param input
     * @param output
     */
    public static void deconstruction(Params input, Container output) {

        GroupContainer upperGroupContainer = input.getUpperGroupContainer();
        ContainerType upperContainerType = upperGroupContainer.getUpperContainerType();
        String upperGroupName = upperGroupContainer.getCurrentGroupName();

        for (NewField field : input.getFields()) {
            //获取当前容器对象
            String currentGroupName = getGroupName(upperGroupName, field.getName());
            ContainerType currentContainerType = ContainerType.getType(field);
            GroupContainer currentGroupContainer = GroupContainer.create(currentContainerType, currentGroupName, upperContainerType);

            //为当前容器创建一个存储数据的对象并将对象的引用保存到 Hash表 中
            output.putContainer(currentGroupContainer);

            //获取当前容器类型的处理器来处理当前字段
            NewFieldHandler newFieldHandler = REGISTER_TYPE_HANDLER.get(currentContainerType);
            if (newFieldHandler != null) {
                input.setCurrentField(field);
                newFieldHandler.process(input, output);
                continue;
            }

            throw new NullPointerException("该类型还没有相应的类型处理器！");
        }
    }

    /**
     * 获取分组名
     *
     * @param params
     * @return
     */
    private static String getGroupName(String... params) {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append(param);
            if (!param.endsWith(Constants.SUFFIX)) {
                sb.append(Constants.SUFFIX);
            }
        }
        return sb.toString();
    }
}

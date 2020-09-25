package com.bugjc.ea.opensdk.mock.data;

import com.bugjc.ea.opensdk.mock.data.parser.*;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模拟对象门面
 *
 * @author aoki
 * @date 2020/9/25
 **/
public class MockDataUtil {
    /**
     * 模拟数据
     *
     * @param clazz 模拟数据类型
     * @return 模拟数据对象
     */
    public static <T> T mock(Class<T> clazz) {
        String prefix = "com.bugjc.";
        List<NewField> fields = Arrays.stream(clazz.getDeclaredFields()).map(field -> new NewField(field.getName(), field.getType(), field.getGenericType())).collect(Collectors.toList());

        GroupContainer initGroupContainer = GroupContainer.create(ContainerType.None, prefix, ContainerType.None);
        Params input = Params.create(initGroupContainer, fields);
        Container output = new Container();
        PropertyParser.deconstruction(input, output);

        Gson gson = new Gson();
        String data = gson.toJson(output.getData());
        return gson.fromJson(data, clazz);
    }
}

package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * 字符串数组类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class StringArrayNewFieldValueConverter implements NewFieldValueConverter<String[]> {
    public final static StringArrayNewFieldValueConverter INSTANCE = new StringArrayNewFieldValueConverter();

    @Override
    public String[] transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }

        String[] strings = new String[RandomUtil.randomInt(1,10)];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = RandomUtil.randomString(4);
        }
        return strings;
    }
}

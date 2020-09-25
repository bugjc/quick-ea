package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * 字符串类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class StringNewFieldValueConverter implements NewFieldValueConverter<String> {

    public final static StringNewFieldValueConverter INSTANCE = new StringNewFieldValueConverter();

    @Override
    public String transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomString(4);
    }
}

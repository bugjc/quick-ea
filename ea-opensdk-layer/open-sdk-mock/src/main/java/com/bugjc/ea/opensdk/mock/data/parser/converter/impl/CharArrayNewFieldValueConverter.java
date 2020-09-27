package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * 字符数组
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class CharArrayNewFieldValueConverter implements NewFieldValueConverter<char[]> {
    public final static CharArrayNewFieldValueConverter INSTANCE = new CharArrayNewFieldValueConverter();

    @Override
    public char[] transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomString(4).toCharArray();
    }
}

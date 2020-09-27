package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * 字节类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class ByteNewFieldValueConverter implements NewFieldValueConverter<Byte> {

    public final static ByteNewFieldValueConverter INSTANCE = new ByteNewFieldValueConverter();

    @Override
    public Byte transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }

        return new Byte(String.valueOf(RandomUtil.randomInt(2)));
    }
}

package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * Float 类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class FloatNewFieldValueConverter implements NewFieldValueConverter<Float> {
    public final static FloatNewFieldValueConverter INSTANCE = new FloatNewFieldValueConverter();

    @Override
    public Float transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomBigDecimal().floatValue();
    }
}

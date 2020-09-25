package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * 双精度浮点类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class DoubleNewFieldValueConverter implements NewFieldValueConverter<Double> {

    public final static DoubleNewFieldValueConverter INSTANCE = new DoubleNewFieldValueConverter();

    @Override
    public Double transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }

        return RandomUtil.randomDouble(4);
    }
}

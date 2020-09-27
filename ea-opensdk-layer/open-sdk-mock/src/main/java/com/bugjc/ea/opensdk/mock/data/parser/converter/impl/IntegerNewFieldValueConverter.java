package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * Integer 类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class IntegerNewFieldValueConverter implements NewFieldValueConverter<Integer> {
    public final static IntegerNewFieldValueConverter INSTANCE = new IntegerNewFieldValueConverter();

    @Override
    public Integer transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomInt(4);
    }
}

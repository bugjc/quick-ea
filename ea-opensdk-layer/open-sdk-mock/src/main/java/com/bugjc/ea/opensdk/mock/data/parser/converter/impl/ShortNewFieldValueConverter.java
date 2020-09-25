package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * 短整型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class ShortNewFieldValueConverter implements NewFieldValueConverter<Short> {

    public final static ShortNewFieldValueConverter INSTANCE = new ShortNewFieldValueConverter();

    @Override
    public Short transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }

        return RandomUtil.randomBigDecimal().shortValue();
    }
}

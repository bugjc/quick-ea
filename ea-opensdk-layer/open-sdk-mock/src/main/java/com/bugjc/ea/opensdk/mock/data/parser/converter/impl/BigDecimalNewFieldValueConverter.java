package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

import java.math.BigDecimal;

/**
 * BigDecimal 类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class BigDecimalNewFieldValueConverter implements NewFieldValueConverter<BigDecimal> {
    public final static BigDecimalNewFieldValueConverter INSTANCE = new BigDecimalNewFieldValueConverter();

    @Override
    public BigDecimal transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomBigDecimal();
    }
}

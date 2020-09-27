package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;



import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

import java.math.BigInteger;

/**
 * BigInteger 类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class BigIntegerNewFieldValueConverter implements NewFieldValueConverter<BigInteger> {
    public final static BigIntegerNewFieldValueConverter INSTANCE = new BigIntegerNewFieldValueConverter();

    @Override
    public BigInteger transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return new BigInteger(String.valueOf(RandomUtil.randomInt(4)));
    }
}

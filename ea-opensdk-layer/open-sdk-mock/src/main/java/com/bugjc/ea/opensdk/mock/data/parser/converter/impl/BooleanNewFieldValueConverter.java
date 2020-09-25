package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * Boolean 类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class BooleanNewFieldValueConverter implements NewFieldValueConverter<Boolean> {
    public final static BooleanNewFieldValueConverter INSTANCE = new BooleanNewFieldValueConverter();

    @Override
    public Boolean transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomBoolean();
    }
}

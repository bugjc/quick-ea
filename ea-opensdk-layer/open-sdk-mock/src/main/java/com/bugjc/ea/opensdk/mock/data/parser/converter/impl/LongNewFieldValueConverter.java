package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

/**
 * Long 类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class LongNewFieldValueConverter implements NewFieldValueConverter<Long> {
    public final static LongNewFieldValueConverter INSTANCE = new LongNewFieldValueConverter();

    @Override
    public Long transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        return RandomUtil.randomLong();
    }
}

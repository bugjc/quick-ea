package com.bugjc.ea.opensdk.mock.data.parser.converter.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.converter.NewFieldValueConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合类型
 *
 * @author aoki
 * @date 2020/9/1
 **/
public class CollectionNewFieldValueConverter implements NewFieldValueConverter<Collection> {
    public final static CollectionNewFieldValueConverter INSTANCE = new CollectionNewFieldValueConverter();

    @Override
    public Collection transform(String value) {
        if (StrUtil.isNotBlank(value)) {
            //TODO 自定义生成策略
            return null;
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < RandomUtil.randomInt(1, 10); i++) {
            list.add(i);
        }
        return list;
    }
}

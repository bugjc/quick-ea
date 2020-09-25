package com.bugjc.ea.opensdk.mock.data.parser.handler.impl;

import cn.hutool.core.util.StrUtil;
import com.bugjc.ea.opensdk.mock.data.parser.Container;
import com.bugjc.ea.opensdk.mock.data.parser.NewField;
import com.bugjc.ea.opensdk.mock.data.parser.Params;
import com.bugjc.ea.opensdk.mock.data.parser.handler.NewFieldHandler;

import java.util.Map;
import java.util.TreeMap;

/**
 * 基础字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public class BasicTypeNewFieldHandler implements NewFieldHandler {

    public final static BasicTypeNewFieldHandler INSTANCE = new BasicTypeNewFieldHandler();

    @Override
    public void process(Params input, Container output) {
        saveData(input, output);
    }

    /**
     * 保存数据
     *
     * @param input
     * @param output
     */
    private void saveData(Params input, Container output) {
        NewField currentField = input.getCurrentField();
        String value = null;
        //基础数据类型的值处理
        output.putContainerValue(currentField.getName(), currentField.getType(), value);
    }

}

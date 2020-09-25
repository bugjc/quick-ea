package com.bugjc.ea.opensdk.mock.data.parser.handler;

import com.bugjc.ea.opensdk.mock.data.parser.Container;
import com.bugjc.ea.opensdk.mock.data.parser.Params;

/**
 * 字段处理器
 *
 * @author aoki
 * @date 2020/9/16
 **/
public interface NewFieldHandler {

    /**
     * 处理函数
     *
     * @param input
     * @param output
     */
    void process(Params input, Container output);
}

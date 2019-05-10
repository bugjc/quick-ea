package com.bugjc.ea.qrcode.service;


import com.bugjc.ea.qrcode.model.CodeExample;

import java.util.List;

/**
 *
 * @author aoki
 * @create 2018/08/06.
 */
public interface CodeExampleService {

    /**
     * 查询全部
     * @return list
     */
    List<CodeExample> findByAll();

}

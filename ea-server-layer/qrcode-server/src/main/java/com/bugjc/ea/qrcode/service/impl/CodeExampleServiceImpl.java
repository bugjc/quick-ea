package com.bugjc.ea.qrcode.service.impl;

import com.bugjc.ea.qrcode.dao.CodeExampleMapper;
import com.bugjc.ea.qrcode.model.CodeExample;
import com.bugjc.ea.qrcode.service.CodeExampleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author aoki
 * @create 2018/08/06.
 */
@Service
public class CodeExampleServiceImpl implements CodeExampleService {

    @Resource
    private CodeExampleMapper tbsCodeExampleMapper;

    @Override
    public List<CodeExample> findByAll() {
        return tbsCodeExampleMapper.selectAll();
    }
}

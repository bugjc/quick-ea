package com.bugjc.ea.qrcode;

import com.bugjc.ea.qrcode.model.CodeExample;
import com.bugjc.ea.qrcode.service.CodeExampleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author liuzh
 * @since 2017/9/2.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(CodeServerApplication.class)
public class CodeExampleServiceTest {

    @Autowired
    private CodeExampleService codeExampleService;

    @Test
    public void test() {
        //查询全部
        codeExampleService.findByAll().forEach(item -> {
            log.info(item.toString());
        });

        //分页
        PageHelper.startPage(0, 10);
        List<CodeExample> list = codeExampleService.findByAll();
        PageInfo<CodeExample> pageInfo = new PageInfo<>(list);
        log.info(pageInfo.toString());
    }
}

package com.bugjc.ea.qrcode.dao;

import com.bugjc.ea.qrcode.model.CodeExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author aoki
 */
@Mapper
public interface CodeExampleMapper{

    /**
     * 查询全部
     * @return list
     */
    @Select("select * from tbs_code_example")
    List<CodeExample> selectAll();
}
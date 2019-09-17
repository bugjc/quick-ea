package com.bugjc.ea.template.model;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
*
*  @author aoki
*/
@TableName("user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1568709740066L;


    /**
    * 主键
    * 主键ID
    * isNullAble:0
    */
    private Long id;

    /**
    * 姓名
    * isNullAble:1
    */
    private String name;

    /**
    * 年龄
    * isNullAble:1
    */
    private Integer age;

    /**
    * 邮箱
    * isNullAble:1
    */
    private String email;

}

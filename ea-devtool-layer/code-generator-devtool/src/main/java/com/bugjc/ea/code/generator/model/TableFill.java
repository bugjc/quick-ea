
package com.bugjc.ea.code.generator.model;

import com.bugjc.ea.code.generator.core.annotation.FieldFill;
import lombok.Data;

/**
 * 字段填充
 *
 * @author hubin
 * @since 2017-06-26
 */
@Data
public class TableFill {

    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 忽略类型
     */
    private FieldFill fieldFill;

}

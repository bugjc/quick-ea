
package com.bugjc.ea.code.generator.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * 表信息，关联到当前字段信息
 *
 * @author YangHu
 * @since 2016/8/30
 */
@Data
@Accessors(chain = true)
public class TableInfo {

    private final Set<String> importPackages = new HashSet<>();
    private boolean convert;
    private String name;
    private String comment;
    private List<TableField> fields;
    private boolean havePrimaryKey;

    /**
     * 增加一个数据库表对应的类名，如：tb_user 表的类名是 User
     */
    private String className;


    /**
     * 公共字段
     */
    private List<TableField> commonFields;
    private String fieldNames;


    public void setFields(List<TableField> fields) {
        this.fields = fields;
        if (CollectionUtils.isNotEmpty(fields)) {
            // 收集导入包信息
            for (TableField field : fields) {
                if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                    //收集字段类型导入包
                    importPackages.add(field.getColumnType().getPkg());
                }

                //TODO 收集字段注解导入包
//                if (field.isKeyFlag()) {
//                    // 主键
//                    if (field.isConvert() || field.isKeyIdentityFlag()) {
//                        importPackages.add(TableId.class.getCanonicalName());
//                    }
//                    // 自增
//                    if (field.isKeyIdentityFlag()) {
//                        importPackages.add(IdType.class.getCanonicalName());
//                    }
//                } else if (field.isConvert()) {
//                    // 普通字段
//                    importPackages.add(TableField.class.getCanonicalName());
//                }
//                if (null != field.getFill()) {
//                    // 填充字段
//                    importPackages.add(TableField.class.getCanonicalName());
//                    importPackages.add(FieldFill.class.getCanonicalName());
//                }
            }
        }
    }

    public void setImportPackages(String pkg) {
        if (importPackages.contains(pkg)) {
        } else {
            importPackages.add(pkg);
        }
    }

    /**
     * 逻辑删除
     */
    public boolean isLogicDelete(String logicDeletePropertyName) {
        return fields.parallelStream().anyMatch(tf -> tf.getName().equals(logicDeletePropertyName));
    }

    /**
     * 转换 filed 实体为 xml mapper 中的 base column 字符串信息
     * {@link /templates/dao/Dao.xml.ftl 使用}
     */
    public String getFieldNames() {
        if (StringUtils.isBlank(fieldNames)
                && CollectionUtils.isNotEmpty(fields)) {
            StringBuilder names = new StringBuilder();
            IntStream.range(0, fields.size()).forEach(i -> {
                TableField fd = fields.get(i);
                if (i == fields.size() - 1) {
                    names.append(fd.getColumnName());
                } else {
                    names.append(fd.getColumnName()).append(", ");
                }
            });
            fieldNames = names.toString();
        }
        return fieldNames;
    }
}

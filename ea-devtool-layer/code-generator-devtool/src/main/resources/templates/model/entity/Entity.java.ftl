package ${template.packagePath};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>
import cn.hutool.core.bean.BeanUtil;
import ${template.dependClasses["model.api"].referencePath!};
import ${template.dependClasses["mybatisPlusAnnotationClass"].referencePath!};
import ${template.dependClasses["serializableClass"].referencePath!};

import lombok.Data;

/**
* <p>
    * ${table.comment!}
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Data
@TableName("${table.name}")
public class ${template.className} implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
     /**
     * ${field.comment}
     */
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
    <#if field.keyIdentityFlag>
    @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
    <#elseif idType??>
    @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
    <#elseif field.convert>
    @TableId("${field.annotationColumnName}")
    </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
     <#if field.convert>
     @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
     <#else>
     @TableField(fill = FieldFill.${field.fill})
     </#if>
    <#elseif field.convert>
    @TableField("${field.annotationColumnName}")
    </#if>
<#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#------------  BEGIN API 转换构造方法  ---------->
    /**
    * 创建 API 数据转存
    * @param param
    */
    public ${template.className}(${template.dependClasses["model.api"].className!} param) {
        BeanUtil.copyProperties(param, this);
        //TODO 数据转换逻辑
    }
<#------------  END 字段循环遍历  ---------->
}

package  ${template.packagePath};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
* ${table.comment!}
* @author ${author}
* @since ${date}
*/
@Data
public class ${template.className} {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    <#if field.keyFlag>
    <#-- ignore -->
    <#else>
    <#-- 字段校验 -->
        <#if field.customMap['NULL'] == "NO">
            <#if field.propertyType == "String">
                /**
                * ${field.comment}
                */
                @NotBlank
                private ${field.propertyType} ${field.propertyName};
            <#elseif field.propertyType == "Integer">
                /**
                * ${field.comment}
                */
                @NotNull
                private ${field.propertyType} ${field.propertyName};
            <#elseif field.propertyType == "Long">
                /**
                * ${field.comment}
                */
                @NotNull
                private ${field.propertyType} ${field.propertyName};
            <#elseif field.propertyType == "LocalDateTime">
            <#-- ignore -->
            <#elseif field.propertyType == "Date">
            <#-- ignore -->
            <#else>
                /**
                * ${field.comment}
                */
                private ${field.propertyType} ${field.propertyName};
            </#if>
        <#else>
            /**
            * ${field.comment}
            */
            private ${field.propertyType} ${field.propertyName};
        </#if>

    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}

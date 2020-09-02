package ${package.packagePath};


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if superControllerClassPackage??>
    import ${superControllerClassPackage};
</#if>

/**
* <p>
    * ${table.comment!} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/

@RestController
@RequestMapping("${package.className}")
<#if superControllerClass??>
    public class ${package.className} extends ${superControllerClass} {
<#else>
    public class ${package.className} {
</#if>


<#--    @Resource-->
<#--    private UserArticleStarRepository business;-->

<#--    /**-->
<#--    * 创建文章 API-->
<#--    *-->
<#--    * @param param     --接口请求参数-->
<#--    * @return          --Result.success()-->
<#--    */-->
<#--    @PostMapping("create")-->
<#--    public Result create(@Validated UserArticleStarCreateApiParam param) {-->
<#--    //business.create(param);-->
<#--    return Result.success();-->
<#--    }-->

}

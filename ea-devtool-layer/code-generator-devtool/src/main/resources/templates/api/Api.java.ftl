package ${package.packagePath};


import com.bugjc.ea.opensdk.http.core.dto.Result;
import ${package.dependClasses["business"].referencePath!};
import ${package.dependClasses["model.api"].referencePath!};
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
* <p>
    * ${table.comment!} API
    * </p>
*
* @author ${author}
* @since ${date}
*/

@RestController
@RequestMapping("${package.className}")
public class ${package.className} {

    @Resource
    private ${package.dependClasses["business"].className!} business;

    /**
    * 创建文章 API
    *
    * @param param     --接口请求参数
    * @return          --Result.success()
    */
    @PostMapping("create")
    public Result create(@Validated ${package.dependClasses["model.api"].className!} param) {
        business.create(param);
        return Result.success();
    }

}

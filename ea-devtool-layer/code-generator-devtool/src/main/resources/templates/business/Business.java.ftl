package ${template.packagePath};

import ${template.dependClasses["model.entity"].referencePath!};
import ${template.dependClasses["model.api"].referencePath!};
import ${template.dependClasses["superServiceClass"].referencePath!};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${template.className} extends ${template.dependClasses["superServiceClass"].className!}<${template.dependClasses["model.entity"].className!}> {

    /**
    * 创建文章
    *
    * @param param
    */
    void create(${template.dependClasses["model.api"].className!} param);
}

package ${package.packagePath};

import ${package.dependClasses["model.entity"].referencePath!};
import ${package.dependClasses["model.api"].referencePath!};
import ${package.dependClasses["superServiceClass"].referencePath!};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${package.className} extends ${package.dependClasses["superServiceClass"].className!}<${package.dependClasses["model.entity"].className!}> {

    /**
    * 创建文章
    *
    * @param param
    */
    void create(${package.dependClasses["model.api"].className!} param);
}
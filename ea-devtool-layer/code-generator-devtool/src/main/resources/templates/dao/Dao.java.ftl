package ${package.packagePath};

import ${package.dependClasses["model.entity"].referencePath!};
import ${package.dependClasses["superMapperClass"].referencePath!};

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${package.className} extends ${package.dependClasses["superMapperClass"].className!}<${package.dependClasses["model.entity"].className!}> {

}

package ${package.packagePath};

import ${package.dependClasses["model.entity"].referencePath!};
import ${superMapperClassPackage};

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${package.className} extends ${superMapperClass}<${package.dependClasses["model.entity"].className!}> {

}

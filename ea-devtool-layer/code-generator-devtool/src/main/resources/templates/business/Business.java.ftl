package ${package.packagePath};

import ${package.dependClasses["model.entity"].referencePath!};
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${package.className} extends ${superServiceClass}<${package.dependClasses["model.entity"].className!}> {

}

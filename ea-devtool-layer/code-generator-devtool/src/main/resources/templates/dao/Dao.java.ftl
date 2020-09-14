package ${template.packagePath};

import ${template.dependClasses["model.entity"].referencePath!};
import ${template.dependClasses["superMapperClass"].referencePath!};

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${template.className} extends ${template.dependClasses["superMapperClass"].className!}<${template.dependClasses["model.entity"].className!}> {

}

package ${package.packagePath};
import ${package.dependClasses["model.entity"].referencePath!};
import ${package.dependClasses["dao"].referencePath!};
import ${package.dependClasses["business"].referencePath!};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${package.className} extends ${superServiceImplClass}<${package.dependClasses["dao"].className!}, ${package.dependClasses["model.entity"].className!}> implements ${package.dependClasses["business"].className!}{

}

package ${package.packagePath};
import ${package.dependClasses["model.entity"].referencePath!};
import ${package.dependClasses["dao"].referencePath!};
import ${package.dependClasses["business"].referencePath!};
import ${package.dependClasses["model.api"].referencePath!};
import ${package.dependClasses["superServiceImplClass"].referencePath!};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${package.className} extends ${package.dependClasses["superServiceImplClass"].className!}<${package.dependClasses["dao"].className!}, ${package.dependClasses["model.entity"].className!}> implements ${package.dependClasses["business"].className!}{

    @Resource
    private ${package.dependClasses["dao"].className!} dao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(${package.dependClasses["model.api"].className!} param) {
        ${package.dependClasses["model.entity"].className!} entity = new ${package.dependClasses["model.entity"].className!}(param);
        dao.insert(entity);
    }
}

package ${template.packagePath};
import ${template.dependClasses["model.entity"].referencePath!};
import ${template.dependClasses["dao"].referencePath!};
import ${template.dependClasses["business"].referencePath!};
import ${template.dependClasses["model.api"].referencePath!};
import ${template.dependClasses["superServiceImplClass"].referencePath!};
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
public class ${template.className} extends ${template.dependClasses["superServiceImplClass"].className!}<${template.dependClasses["dao"].className!}, ${template.dependClasses["model.entity"].className!}> implements ${template.dependClasses["business"].className!}{

    @Resource
    private ${template.dependClasses["dao"].className!} dao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(${template.dependClasses["model.api"].className!} param) {
        ${template.dependClasses["model.entity"].className!} entity = new ${template.dependClasses["model.entity"].className!}(param);
        dao.insert(entity);
    }
}

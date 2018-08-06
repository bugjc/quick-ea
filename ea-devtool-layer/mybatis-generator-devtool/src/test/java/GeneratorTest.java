import generator.config.GlobalConfiguration;
import generator.constant.GeneratorKeyConstant;
import generator.service.IGenerator;
import org.junit.Test;
import org.mybatis.generator.config.GeneratedKey;
import org.springframework.beans.factory.annotation.Autowired;

public class GeneratorTest extends Tester {

    @Autowired
    public GlobalConfiguration globalConfiguration;
    @Autowired
    public IGenerator generator;

    /**
     * gen code example
     */
    @Test
    public void genCode(){

       GeneratedKey generatedKey = new GeneratedKey("id", GeneratorKeyConstant.key.SqlServer.name(), true, null);
       generator.genCode(globalConfiguration,generatedKey, GeneratorKeyConstant.key.MySql.name(),"tbs_code_example","CodeExample");
       // 生成mssql数据库的mybatis文件
//        GeneratedKey mssqlGeneratedKey = new GeneratedKey("id", GeneratorKeyConstant.key.SqlServer.name(), true, null);
//        generator.genCode(globalConfiguration,mssqlGeneratedKey,GeneratorKeyConstant.key.SqlServer.name(),"tCard","Card");

    }

}

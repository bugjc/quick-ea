package springboot.maven.template.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.CommonResultCode;
import springboot.maven.template.api.ApiClient;
import springboot.maven.template.env.EnvUtil;
import springboot.maven.template.web.io.user.UserLoginBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 用户登录业务
 * @author aoki
 * @date ${date}
 */
@Slf4j
public class UserLoginWebTest {

    private ApiClient apiClient =  new ApiClient(EnvUtil.EnvType.DEV);

    /**
     * 用户登录测试
     */
    @Test
    public void testUserLogin() {
        UserLoginBody.RequestBody requestBody = new UserLoginBody.RequestBody();
        requestBody.setUsername("Jack");
        requestBody.setPassword("123456");

        String bodyData = JSON.toJSONString(requestBody);
        log.info("Parameter:{}",bodyData);

        Result result = apiClient.doPost(ApiClient.ContentPath.USER_LOGIN_PATH, bodyData);
        if (result.getCode() != CommonResultCode.SUCCESS.getCode()){
            log.error("系统错误：{}",result.getMessage());
            return;
        }

        log.info(result.toString());
        JSONObject data = (JSONObject) result.getData();
        log.info("ResponseResult：{}",data.toJSONString());
    }
}

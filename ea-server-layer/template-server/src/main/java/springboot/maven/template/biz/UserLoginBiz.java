package springboot.maven.template.biz;


import com.bugjc.ea.opensdk.http.core.dto.Result;
import com.bugjc.ea.opensdk.http.core.dto.ResultGenerator;

import springboot.maven.template.core.enums.UserLoginFailCode;
import springboot.maven.template.service.UserService;
import springboot.maven.template.web.io.user.UserLoginBody;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 用户登录业务
 * @author aoki
 * @create 2018/12/14.
 */
@Slf4j
@Service
public class UserLoginBiz {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param requestBody
     * @return
     */
    public Result login(UserLoginBody.RequestBody requestBody){
        //1.生成默认应答信息
        UserLoginBody.ResponseBody responseBody = new UserLoginBody.ResponseBody();
        responseBody.setFailCode(UserLoginFailCode.Success.getCode());

        try {
            //2.执行业务逻辑
            responseBody = userService.login(requestBody,responseBody);
        }catch (Exception ex){
            //3.设置运行中失败默认错误
            responseBody.setFailCode(UserLoginFailCode.ERROR.getCode());
        }
        return ResultGenerator.genSuccessResult(responseBody);
    }


}

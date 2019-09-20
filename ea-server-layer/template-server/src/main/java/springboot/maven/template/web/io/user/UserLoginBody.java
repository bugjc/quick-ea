package springboot.maven.template.web.io.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 附近充电站列表接口请求响应参数对象
 * @author aoki
 * @date ${date}
 */
@Data
public class UserLoginBody implements Serializable {

    /**
     * 请求参数对象
     */
    @Data
    public static class RequestBody implements Serializable{
        @NotNull(message = "用户名不能为空")
        private String username;
        @NotNull(message="用户密码不能为空")
        private String password;
    }

    /**
     * 应答参数对象
     */
    @Data
    public static class ResponseBody implements Serializable{

        //状态
        private int failCode = 0;
        //充电站列表信息
        private UserInfo userInfo;

        @Data
        public static class UserInfo implements Serializable{
            //昵称
            private String nickname;
            //年龄
            private String age;
        }
    }


}

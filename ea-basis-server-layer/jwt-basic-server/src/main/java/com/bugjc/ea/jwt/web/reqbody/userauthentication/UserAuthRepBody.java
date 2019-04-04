package com.bugjc.ea.jwt.web.reqbody.userauthentication;

import com.bugjc.ea.jwt.web.reqbody.userregister.RegisterGroup;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserAuthRepBody {

    @Valid
    @NotNull(message = "用户信息不能为空",groups = AuthTokenGroup.class)
    private User user;

    @Data
    public class User implements Serializable {
        @NotBlank(message = "用户名称不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
    }
}

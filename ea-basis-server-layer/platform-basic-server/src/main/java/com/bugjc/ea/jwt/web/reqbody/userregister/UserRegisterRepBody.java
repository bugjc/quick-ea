package com.bugjc.ea.auth.web.reqbody.userregister;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserRegisterRepBody {

    @NotBlank(message = "用户名称不能为空",groups = RegisterGroup.class)
    private String username;
    @NotBlank(message = "密码不能为空",groups = RegisterGroup.class)
    private String password;
    @NotBlank(message = "确定密码不能为空",groups = RegisterGroup.class)
    private String confirmPassword;
}

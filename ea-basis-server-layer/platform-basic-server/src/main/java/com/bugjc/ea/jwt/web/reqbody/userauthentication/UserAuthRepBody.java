package com.bugjc.ea.auth.web.reqbody.userauthentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAuthRepBody {

    @NotBlank(message = "用户名称不能为空",groups = AuthTokenGroup.class)
    private String username;
    @NotBlank(message = "密码不能为空",groups = AuthTokenGroup.class)
    private String password;
}

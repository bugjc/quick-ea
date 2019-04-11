package com.bugjc.ea.jwt.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.bugjc.ea.jwt.core.dto.Result;
import com.bugjc.ea.jwt.core.dto.ResultGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bugjc.ea.jwt.core.security.JwtTokenUtil;
import com.bugjc.ea.jwt.core.security.JwtUser;

@RestController
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 根据token获取用户授权认证的用户对象
     * @param request
     * @return
     */
    @GetMapping(value = "user")
    public Result getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return ResultGenerator.genSuccessResult(userDetailsService.loadUserByUsername(username));
    }

}

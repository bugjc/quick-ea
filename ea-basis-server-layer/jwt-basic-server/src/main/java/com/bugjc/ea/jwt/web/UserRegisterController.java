package com.bugjc.ea.jwt.web;

import com.bugjc.ea.jwt.core.dto.Result;
import com.bugjc.ea.jwt.core.dto.ResultGenerator;
import com.bugjc.ea.jwt.core.exception.AuthenticationException;
import com.bugjc.ea.jwt.core.security.JwtAuthenticationRequest;
import com.bugjc.ea.jwt.core.security.JwtTokenUtil;
import com.bugjc.ea.jwt.core.dto.JwtAuthenticationResponse;
import com.bugjc.ea.jwt.web.reqbody.userregister.RegisterGroup;
import com.bugjc.ea.jwt.web.reqbody.userregister.UserRegisterRepBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserRegisterController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 注册用户
     * @param userRegisterRepBody
     * @return
     * @throws AuthenticationException
     */
    @PostMapping(value = "register")
    public Result register(@Validated({RegisterGroup.class}) @RequestBody UserRegisterRepBody userRegisterRepBody) throws AuthenticationException {



        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userRegisterRepBody.getUser().getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(token));
    }
}

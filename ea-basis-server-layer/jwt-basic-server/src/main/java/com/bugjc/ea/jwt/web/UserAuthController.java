package com.bugjc.ea.jwt.web;

import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bugjc.ea.http.opensdk.core.dto.Result;
import com.bugjc.ea.http.opensdk.core.dto.ResultCode;
import com.bugjc.ea.http.opensdk.core.dto.ResultGenerator;
import com.bugjc.ea.jwt.core.exception.AuthenticationException;
import com.bugjc.ea.jwt.core.dto.JwtAuthenticationResponse;
import com.bugjc.ea.jwt.web.reqbody.userauthentication.AuthTokenGroup;
import com.bugjc.ea.jwt.web.reqbody.userauthentication.UserAuthRepBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.bugjc.ea.jwt.core.security.JwtTokenUtil;
import com.bugjc.ea.jwt.core.security.JwtUser;

@Slf4j
@RestController
public class UserAuthController {

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
     * 用户认证
     * @param userAuthRepBody
     * @return
     * @throws AuthenticationException
     */
    @PostMapping(value = "${jwt.route.authentication.path}")
    public Result authToken(@Validated({AuthTokenGroup.class}) @RequestBody UserAuthRepBody userAuthRepBody) throws AuthenticationException {
        authenticate(userAuthRepBody.getUsername(), userAuthRepBody.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthRepBody.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(token));
    }

    /**
     * 校验token成功
     * @return
     */
    @PostMapping(value = "${jwt.route.authentication.verify}")
    public Result verifyToken(){
        log.info("TODO Token 校验成功");
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 刷新token
     * @param request
     * @return
     */
    @GetMapping(value = "${jwt.route.authentication.refresh}")
    public Result refreshToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResultGenerator.genFailResult("刷新token失败");
        }
    }

    /**
     * 授权认证
     * @param username
     * @param password
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException(ResultCode.UNAUTHORIZED.getCode(),"用户已被禁用!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException(ResultCode.UNAUTHORIZED.getCode(),"认证失败!", e);
        }
    }
}

package com.bugjc.ea.jwt.web;

import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bugjc.ea.jwt.core.dto.Result;
import com.bugjc.ea.jwt.core.dto.ResultGenerator;
import com.bugjc.ea.jwt.core.exception.AuthenticationException;
import com.bugjc.ea.jwt.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import com.bugjc.ea.jwt.core.dto.ResultCode;
import com.bugjc.ea.jwt.core.security.JwtAuthenticationRequest;
import com.bugjc.ea.jwt.core.security.JwtTokenUtil;
import com.bugjc.ea.jwt.core.security.JwtUser;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @PostMapping(value = "${jwt.route.authentication.path}")
    public Result createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(token));
    }

    @GetMapping(value = "${jwt.route.authentication.refresh}")
    public Result refreshAndGetAuthenticationToken(HttpServletRequest request) {
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

    @ExceptionHandler({AuthenticationException.class})
    public Result handleAuthenticationException(AuthenticationException e) {
        return ResultGenerator.genFailResult(ResultCode.UNAUTHORIZED.getCode(),e.getMessage());
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}

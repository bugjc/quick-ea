package com.bugjc.ea.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.bugjc.ea.jwt.model.User;
import com.bugjc.ea.jwt.core.security.JwtUserFactory;
import com.bugjc.ea.jwt.dao.UserRepository;

import javax.annotation.Resource;

@Service("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}

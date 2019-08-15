package com.bugjc.ea.jwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bugjc.ea.jwt.model.User;

/**
 * @author aoki
 */
public interface UserDao extends JpaRepository<User, Long> {
    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}

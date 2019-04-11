package com.bugjc.ea.jwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bugjc.ea.jwt.model.User;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

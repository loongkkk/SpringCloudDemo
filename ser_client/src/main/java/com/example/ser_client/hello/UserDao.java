package com.example.ser_client.hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Boolean existsByUsername(String username);

    Boolean existsByUserId(Long userId);

    User getOneByUserId(Long userId);
}

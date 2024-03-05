package com.springcloud.msvc.users.dao;

import com.springcloud.msvc.users.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<User, Long> {

    @Query("select u from User u where u.email=?1")
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);
}

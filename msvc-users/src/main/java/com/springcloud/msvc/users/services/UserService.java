package com.springcloud.msvc.users.services;

import com.springcloud.msvc.users.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User save(User User);
    void delete(Long id);
    List<User> listByGivenIds(Iterable<Long> ids);
    Optional<User> getUserByEmail(String email);

    boolean isExistingEmail(String email);
}

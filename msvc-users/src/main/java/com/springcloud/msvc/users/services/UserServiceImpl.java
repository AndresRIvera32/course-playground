package com.springcloud.msvc.users.services;

import com.springcloud.msvc.users.client.CourseRestClient;
import com.springcloud.msvc.users.dao.UserDao;
import com.springcloud.msvc.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao repository;

    @Autowired
    private CourseRestClient restClient;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return (List<User>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User User) {
        return repository.save(User);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
        restClient.deleteCourseUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listByGivenIds(Iterable<Long> ids) {
        return (List<User>) repository.findAllById(ids);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    @Override
    public boolean isExistingEmail(String email) {
        return repository.existsByEmail(email);
    }


}

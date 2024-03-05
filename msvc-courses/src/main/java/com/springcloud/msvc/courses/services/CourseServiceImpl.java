package com.springcloud.msvc.courses.services;

import com.springcloud.msvc.courses.clients.UserClientRest;
import com.springcloud.msvc.courses.models.User;
import com.springcloud.msvc.courses.models.entity.Course;
import com.springcloud.msvc.courses.models.entity.CourseUser;
import com.springcloud.msvc.courses.repositories.CourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao repository;

    @Autowired
    private UserClientRest clientRest;


    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return (List<Course>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseByIdUsers(Long id) {
        Optional<Course> o = repository.findById(id);
        if (o.isPresent()) {
            Course Course = o.get();
            if (!Course.getCourseUsers().isEmpty()) {
                List<Long> ids = Course.getCourseUsers().stream().map(cu -> cu.getUserId())
                        .collect(Collectors.toList());

                List<User> Users = clientRest.getStudentsByCourse(ids);
                Course.setUsers(Users);
            }
            return Optional.of(Course);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Course save(Course Course) {
        return repository.save(Course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        repository.deleteCourseUserById(id);
    }

    @Transactional
    public Optional<User> assignUser(Long userId, Long CourseId) {
        Optional<Course> o = repository.findById(CourseId);
        if (o.isPresent()) {
            User UserMsvc = clientRest.detailUserById(userId);

            Course Course = o.get();
            CourseUser CourseUser = new CourseUser();
            CourseUser.setUserId(UserMsvc.getId());

            Course.addCourseUser(CourseUser);
            repository.save(Course);
            return Optional.of(UserMsvc);
        }
        return Optional.empty();
    }


    @Transactional
    public Optional<User> createUser(User User, Long CourseId) {

        Optional<Course> o = repository.findById(CourseId);
        if (o.isPresent()) {
            User UserNuevoMsvc = clientRest.create(User);

            Course Course = o.get();
            CourseUser CourseUser = new CourseUser();
            CourseUser.setUserId(UserNuevoMsvc.getId());

            Course.addCourseUser(CourseUser);
            repository.save(Course);
            return Optional.of(UserNuevoMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(User User, Long CourseId) {
        Optional<Course> o = repository.findById(CourseId);
        if (o.isPresent()) {
            User UserMsvc = clientRest.detailUserById(User.getId());

            Course Course = o.get();
            CourseUser CourseUser = new CourseUser();
            CourseUser.setUserId(UserMsvc.getId());
            Course.removeCourseUser(CourseUser);
            repository.save(Course);
            return Optional.of(UserMsvc);
        }
        return Optional.empty();
    }
}

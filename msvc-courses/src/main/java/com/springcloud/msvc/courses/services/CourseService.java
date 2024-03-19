package com.springcloud.msvc.courses.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springcloud.msvc.courses.models.User;
import com.springcloud.msvc.courses.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();
    Optional<Course> getCourseById(Long id);
    Optional<Course> getCourseByIdUsers(Long id);
    Course save(Course Course) throws JsonProcessingException;
    void delete(Long id);

    void deleteCourseUserById(Long id);

    Optional<User> assignUser(Long userId, Long CourseId);
    Optional<User> createUser(User User, Long CourseId);
    Optional<User> deleteUser(User User, Long CourseId);
}

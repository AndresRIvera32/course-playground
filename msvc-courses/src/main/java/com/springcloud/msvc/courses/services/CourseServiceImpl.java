package com.springcloud.msvc.courses.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.msvc.courses.clients.UserClientRest;
import com.springcloud.msvc.courses.models.User;
import com.springcloud.msvc.courses.models.entity.Course;
import com.springcloud.msvc.courses.models.entity.CourseUser;
import com.springcloud.msvc.courses.models.entity.OutboxEvent;
import com.springcloud.msvc.courses.repositories.CourseDao;
import com.springcloud.msvc.courses.repositories.OutboxDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    private final UserClientRest clientRest;

    private final OutboxDao outboxDao;

    private final ObjectMapper objectMapper;

    public CourseServiceImpl(CourseDao courseDao, UserClientRest clientRest, OutboxDao outboxDao, ObjectMapper objectMapper){
        this.courseDao = courseDao;
        this.clientRest = clientRest;
        this.outboxDao = outboxDao;
        this.objectMapper = objectMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return (List<Course>) courseDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return courseDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseByIdUsers(Long id) {
        Optional<Course> o = courseDao.findById(id);
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
    public Course save(Course course) throws JsonProcessingException {
        OutboxEvent event = new OutboxEvent("CourseCreated", objectMapper.writeValueAsString(course));
        outboxDao.save(event);
        return courseDao.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        courseDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        courseDao.deleteCourseUserById(id);
    }

    @Transactional
    public Optional<User> assignUser(Long userId, Long CourseId) {
        Optional<Course> o = courseDao.findById(CourseId);
        if (o.isPresent()) {
            User UserMsvc = clientRest.detailUserById(userId);

            Course Course = o.get();
            CourseUser CourseUser = new CourseUser();
            CourseUser.setUserId(UserMsvc.getId());

            Course.addCourseUser(CourseUser);
            courseDao.save(Course);
            return Optional.of(UserMsvc);
        }
        return Optional.empty();
    }


    @Transactional
    public Optional<User> createUser(User User, Long CourseId) {

        Optional<Course> o = courseDao.findById(CourseId);
        if (o.isPresent()) {
            User UserNuevoMsvc = clientRest.create(User);

            Course Course = o.get();
            CourseUser CourseUser = new CourseUser();
            CourseUser.setUserId(UserNuevoMsvc.getId());

            Course.addCourseUser(CourseUser);
            courseDao.save(Course);

            return Optional.of(UserNuevoMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(User User, Long CourseId) {
        Optional<Course> o = courseDao.findById(CourseId);
        if (o.isPresent()) {
            User UserMsvc = clientRest.detailUserById(User.getId());

            Course Course = o.get();
            CourseUser CourseUser = new CourseUser();
            CourseUser.setUserId(UserMsvc.getId());
            Course.removeCourseUser(CourseUser);
            courseDao.save(Course);
            return Optional.of(UserMsvc);
        }
        return Optional.empty();
    }
}

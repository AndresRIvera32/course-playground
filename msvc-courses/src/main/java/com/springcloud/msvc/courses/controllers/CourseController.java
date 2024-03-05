package com.springcloud.msvc.courses.controllers;

import com.springcloud.msvc.courses.models.User;
import com.springcloud.msvc.courses.models.entity.Course;
import com.springcloud.msvc.courses.services.CourseService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(service.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailCourse(@PathVariable Long id) {
        Optional<Course> optionalCourse = service.getCourseByIdUsers(id);
        if (optionalCourse.isPresent()) {
            return ResponseEntity.ok(optionalCourse.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Course Course, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }
        Course CourseDb = service.save(Course);
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody Course Course, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validate(result);
        }
        Optional<Course> o = service.getCourseById(id);
        if (o.isPresent()) {
            Course CourseDb = o.get();
            CourseDb.setName(Course.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(CourseDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Course> optionalCourse = service.getCourseById(id);
        if (optionalCourse.isPresent()) {
            service.delete(optionalCourse.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{courseId}/user/{userId}/assign-user")
    public ResponseEntity<?> assignUser(@PathVariable Long userId, @PathVariable Long courseId) {
        Optional<User> optionalUser;
        try {
            optionalUser = service.assignUser(userId, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "The user does not exist because " +
                            "The Id or error in the communication: " + e.getMessage()));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> optionalUser;
        try {
            optionalUser = service.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "The user could not be created " +
                            "or there was an error in the communication: " + e.getMessage()));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{courseId}")
    public ResponseEntity<?> deleteUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> optionalUser;
        try {
            optionalUser = service.deleteUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "The user does not exit because " +
                            "the id or error in the communication: " + e.getMessage()));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-course-user/{id}")
    public ResponseEntity<?> deleteCourseUserById(@PathVariable Long id){
        service.deleteCourseUserById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "The field " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}

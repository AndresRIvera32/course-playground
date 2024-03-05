package com.springcloud.msvc.users.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="msvc-courses", url = "host.docker.internal:8002")
//@FeignClient(name="msvc-courses", url = "${msvc.courses.url}")

@FeignClient(name="msvc-courses")
public interface CourseRestClient {
    @DeleteMapping("/api/courses/delete-course-user/{id}")
    void deleteCourseUserById(@PathVariable Long id);
}

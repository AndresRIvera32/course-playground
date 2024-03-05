package com.springcloud.msvc.courses.clients;

import com.springcloud.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@FeignClient(name="msvc-users", url="localhost:8001")
//@FeignClient(name="msvc-users", url="${msvc.users.url}")

@FeignClient(name="msvc-users")
public interface UserClientRest {

    @GetMapping("/api/users/{id}")
    User detailUserById(@PathVariable Long id);

    @PostMapping("/api/users/")
    User create(@RequestBody User User);

    @GetMapping("/api/users/users-by-course")
    List<User> getStudentsByCourse(@RequestParam Iterable<Long> ids);
}

package com.springcloud.msvc.users.controllers;

import com.springcloud.msvc.users.model.User;
import com.springcloud.msvc.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment env;

    @GetMapping
    public Map<String, Object> getAllUsers() {
        Map<String, Object> map = new HashMap<>();
        map.put("users", service.getAllUsers());
        map.put("podInfo: ", env.getProperty("MY_POD_NAME") + " : " + env.getProperty("MY_POD_IP"));
        return map;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailUserById(@PathVariable Long id) {
        Optional<User> UserOptional = service.getUserById(id);
        if (UserOptional.isPresent()) {
            return ResponseEntity.ok(UserOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody User User, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }

        if (!User.getEmail().isEmpty() && service.isExistingEmail(User.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje", "Ya existe un User con ese correo electronico!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(User));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody User User, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            return validar(result);
        }

        Optional<User> o = service.getUserById(id);
        if (o.isPresent()) {
            User UserDb = o.get();
            if (!User.getEmail().isEmpty() &&
                    !User.getEmail().equalsIgnoreCase(UserDb.getEmail()) &&
                    service.getUserByEmail(User.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("mensaje", "There is already an user with that email registered!!"));
            }

            UserDb.setName(User.getName());
            UserDb.setEmail(User.getEmail());
            UserDb.setPassword(User.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(UserDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> o = service.getUserById(id);
        if (o.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-by-course")
    public ResponseEntity<?> getStudentsByCourse(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listByGivenIds(ids));
    }

    @GetMapping("/crash")
    public void emulateCrashEndpoint(){
        ((ConfigurableApplicationContext)context).close();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
    @GetMapping("/authorized")
    public Map<String, Object> authorized(@RequestParam(name = "code") String code){
        return Collections.singletonMap("code", code);
    }
}

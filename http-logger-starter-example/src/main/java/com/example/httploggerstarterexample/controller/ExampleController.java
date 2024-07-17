package com.example.httploggerstarterexample.controller;

import com.example.httploggerstarterexample.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для обработки тестовых HTTP-запросов.
 * <p>
 * Этот контроллер предоставляет две точки для демонстрации работы приложения:
 * </p>
 */
@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
@Slf4j
public class ExampleController {

    @GetMapping(path = "/hello")
    public ResponseEntity<String> hello(@RequestParam(defaultValue = "User") String name) {
        return ResponseEntity.ok("Hello, " + name + "!");
    }

    @PostMapping(path = "/user")
    public ResponseEntity<User> user(@RequestBody User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "поле firstName или lastName равно null");
        }

        return ResponseEntity.ok(user);
    }
}

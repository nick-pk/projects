package com.app.auth.controller;

import com.app.auth.entity.User;
import com.app.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody User user) {
        return userService.verify(user);
    }

    @PostMapping("/validate-token")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        return userService.validateToken(token);
    }
}

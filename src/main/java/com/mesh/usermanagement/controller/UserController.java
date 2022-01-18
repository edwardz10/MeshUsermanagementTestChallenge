package com.mesh.usermanagement.controller;

import com.mesh.usermanagement.model.User;
import com.mesh.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/users")
    public User createUser(@RequestBody @Valid User user) {
        log.info("Request to create user: {}", user);
        return userService.createUser(user);
    }
}

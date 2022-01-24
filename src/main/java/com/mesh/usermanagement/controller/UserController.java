package com.mesh.usermanagement.controller;

import com.mesh.usermanagement.exception.UserManagementServiceException;
import com.mesh.usermanagement.model.User;
import com.mesh.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;
  @PostMapping(value = "/users", produces = "application/json")
  public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    log.info("Request to create user: {}", user);
    User createdUser = userService.createUser(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @PutMapping(value = "/users/{userId}", produces = "application/json")
  public ResponseEntity<User> getUser(@PathVariable(value = "userId") String userId, @RequestBody @Valid User user)
      throws UserManagementServiceException {
    log.info("Request to get a user by Id: {}", userId);
    User updatedUser = userService.updateUser(userId, user);
    return updatedUser == null
        ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        : ResponseEntity.status(HttpStatus.OK).body(updatedUser);
  }

  @GetMapping(value = "/users", produces = "application/json")
  public ResponseEntity<List<User>> getUserAllUsers() {
    log.info("Request to get all users");
    return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
  }

  @GetMapping(value = "/users/{userId}", produces = "application/json")
  public ResponseEntity<User> getUser(@PathVariable(value = "userId") String userId) {
    log.info("Request to get a user by Id: {}", userId);
    User user = userService.getUser(userId);
    return user == null
      ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
      : ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @DeleteMapping(value = "/users/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") String userId) {
    log.info("Request to delete a user by Id: {}", userId);
    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}

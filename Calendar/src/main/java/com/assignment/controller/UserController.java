package com.assignment.controller;

import com.assignment.dao.User;
import com.assignment.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Add a new user")
    @PostMapping("/add-user")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @ApiOperation(value = "Fetch all users")
    @GetMapping("/fetch-all-users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "Get a user")
    @GetMapping("/fetch-user/{userId}")
    public Optional<User> getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }
}

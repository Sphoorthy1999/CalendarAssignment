package com.assignment.service;

import com.assignment.dao.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user);

    List<User> getAllUsers();

    Optional<User> getUser(Long userId);
}

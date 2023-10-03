package com.example.demo.services;

import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);

    Optional<User> readUsers(Long id);

    void deleteUser(User user);
    void setStatus(Long id, boolean status);

    List<User> findAllEnable();
    List<User> findAllUsers();


}

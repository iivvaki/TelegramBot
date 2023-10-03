package com.example.demo.services.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User addUser(User user) {
        if (user == null){
            return null;
        }
        boolean isPresent = userRepository.existsById(user.getId());
        if (!isPresent){
            return userRepository.save(user);
        }else return null;
    }

    @Override
    public Optional<User> readUsers(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(User user) {
        if(user != null)
            userRepository.delete(user);
    }

    @Override
    public void setStatus(Long id, boolean status) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u->{u.setEnable(status); userRepository.save(u);});
    }

    @Override
    public List<User> findAllEnable() {
        return userRepository.findAllEnable();
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}

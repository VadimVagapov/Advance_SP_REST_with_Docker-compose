package ru.kata.spring.boot_security.demo.service;


import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    void add(User user);
    void update(User user, long id);

    User SearchUser(long id);

    List<User> findByUsername(String name);
    void remove(long id);
    List<User> getListUsers(String count);

    void validation(User user, Errors errors);

}

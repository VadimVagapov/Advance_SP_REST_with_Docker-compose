package ru.kata.spring.boot_security.demo.service;


import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {
    void add(User user);

    User SearchUser(long id);

    User findByUsername(String name);
    void remove(long id);
    List<User> getAllUsers();
    void validation(User user, Errors errors);

}

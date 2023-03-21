package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exception.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestUserControl {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public RestUserControl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> list = userService.getAllUsers();
        return list;
    }

    @GetMapping("/user")
    public User getUserByUserName(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return user;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
    User user = userService.SearchUser(id);
    if (user == null) {
        throw new NoSuchUserException(String.format("User with ID = %d not found in Database", id));
    }
        return user;
    }

    @PostMapping("/users")
    public User addNewUser(@RequestBody User user) {
        userService.add(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        userService.add(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.remove(id);
        return "User with ID = " + id + " was deleted";
    }
}

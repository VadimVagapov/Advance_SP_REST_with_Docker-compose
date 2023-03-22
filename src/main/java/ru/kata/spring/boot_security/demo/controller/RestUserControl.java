package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = userService.getAllUsers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByUserName(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
    User user = userService.SearchUser(id);
    if (user == null) {
        throw new NoSuchUserException(String.format("User with ID = %d not found in Database", id));
    }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public HttpStatus addNewUser(@RequestBody User user) {
        userService.add(user);
        return HttpStatus.OK;
    }

    @PutMapping("/users")
    public HttpStatus updateUser(@RequestBody User user) {
        userService.add(user);
        return HttpStatus.OK;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.remove(id);
        return new ResponseEntity<>(String.format("User with ID = %d was deleted", id), HttpStatus.OK);
    }
}

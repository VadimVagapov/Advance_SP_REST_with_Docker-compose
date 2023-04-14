package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    public RestUserControl(UserService userService, RoleService roleService) {
        this.roleService = roleService;
        this.userService = userService;
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
        User user = userService.searchUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //здесь не получается в валидацию
    //при создании кажется используется метод PUT хотя в скрипте я прописал что нужен POST метод
    @PostMapping("/users")
    public ResponseEntity<String> addNewUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>((String.format("User with name %s exist yet", user.getUsername())), HttpStatus.BAD_REQUEST);
        } else {
            userService.add(user);
            return new ResponseEntity<>("User create", HttpStatus.CREATED);
        }
    }

    @PutMapping("/users")
    public HttpStatus updateUser(@RequestBody User user) {
        if (user.getRoles().size() == 0) {
            user.setRoles(userService.findByUsername(user.getUsername()).getRoles());
        }
        user.setRoles(roleService.searchRolesOnUser(user.getRoles()));
        userService.add(user);
        return HttpStatus.OK;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.remove(id);
        return new ResponseEntity<>(String.format("User with ID = %d was deleted", id), HttpStatus.OK);
    }
}

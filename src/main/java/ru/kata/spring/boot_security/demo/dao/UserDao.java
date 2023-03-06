package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    void add(User user);
    void update(User user, long id);

    List<User> findByUsername(String name);

    User SearchUser(long id);
    void remove(long id);
    List<User> getListUsers(String count);
    Set<Role> getDefaultRoles();
}

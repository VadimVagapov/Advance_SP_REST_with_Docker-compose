package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getShortNames(Set<Role> roles);
    Role getRoleById(long id);

    Set<Role> getAllRole();
}

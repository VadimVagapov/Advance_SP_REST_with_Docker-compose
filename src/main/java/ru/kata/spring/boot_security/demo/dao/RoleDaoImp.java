package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleDaoImp implements RoleDao {
    public Set<Role> getShortNames(Set<Role> roles) {
        roles.forEach(x -> x.getName().replace("ROLE_", "").concat("  "));
        return roles;
    }
}

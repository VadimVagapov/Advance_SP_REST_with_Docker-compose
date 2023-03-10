package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.stream.Collectors;

@Qualifier("role")
@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;
    public Set<Role> getShortNames(Set<Role> roles) {
        roles.forEach(x -> x.getName().replace("ROLE_", "").concat("  "));
        return roles;
    }

    public Role getRoleById(long id) {
        return entityManager.find(Role.class, id);
    }

    public Set<Role> getAllRole() {
        return (Set<Role>) entityManager.createQuery("from Role").getResultStream().collect(Collectors.toSet());
    }
}

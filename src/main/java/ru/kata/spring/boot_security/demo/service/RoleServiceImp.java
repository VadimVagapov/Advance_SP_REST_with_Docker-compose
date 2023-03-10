package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleDao roleDao;

    public Set<Role> getAllRole() {
        return roleDao.getAllRole();
    }

    @Override
    public Set<Role> getShortNames(Set<Role> roles) {
        return roleDao.getShortNames(roles);
    }

    @Override
    public Role getRoleById(long id) {
        return roleDao.getRoleById(id);
    }
}

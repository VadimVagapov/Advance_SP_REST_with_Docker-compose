package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImp implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> searchRolesOnUser(List<Role> listBefore) {
        List<Role> listAfter = new ArrayList<>();
        if (listBefore.size() == 2) {
            return getAllRole();
        } else if (listBefore.get(0).getId() == 1) {
            listAfter.add(getRoleById(1));
        } else {
            listAfter.add(getRoleById(2));
        }
        return listAfter;
    }

    @Override
    public void addRolesAfterStart() {
        roleRepository.save(new Role(1L, "ROLE_ADMIN"));
        roleRepository.save(new Role(2L, "ROLE_USER"));
    }

    @Override
    public Role getRoleById(long id) {
        Optional<Role> opti = roleRepository.findById(id);
        if (opti.isPresent()) {
            return opti.get();
        }
        return null;
    }
}

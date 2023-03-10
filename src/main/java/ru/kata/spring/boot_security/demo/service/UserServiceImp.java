package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired private UserDao userDao;

    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    public void update(User user, long id) {
        userDao.update(user, id);
    }

    @Override
    public User SearchUser(long id) {
        return userDao.SearchUser(id);
    }

    @Override
    public List<User> findByUsername(String name) {
        return userDao.findByUsername(name);
    }

    @Override
    public void remove(long id) {
        userDao.remove(id);
    }

    @Override
    public void validation(User user, Errors errors) {
        try {
            loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignore) {
            return;
        }
        errors.rejectValue("username", "", String.format("User with name %s exist yet", user.getUsername()));
    }

    @Override
    public List<User> getListUsers(String count) {
        return userDao.getListUsers(count);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).get(0);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.getAuthorities());
    }
}

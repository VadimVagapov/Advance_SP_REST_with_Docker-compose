package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.security.MyUserDetails;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public User SearchUser(long id) {
        User user = null;
        Optional<User> opti = userRepository.findById(id);
        if(opti.isPresent()) {
            user = opti.get();
        }
        return user;
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findUserByUsername(name);
    }

    @Override
    public void remove(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
        //return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.getAuthorities());
    }
}

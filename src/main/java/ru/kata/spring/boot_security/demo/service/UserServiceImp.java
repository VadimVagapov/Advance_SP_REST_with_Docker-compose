package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.exception.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.security.MyUserDetails;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {

    private UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.saveAndFlush(user);
    }


    @Override
    public User searchUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchUserException(String.format("User with ID = %d not found in Database", id));
        }
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

    //метод для проверки на валидацию username при создании нового юзера/ почему то не получается его реализовать(
    //если такой юзер уже есть то в errors(BindingResult) кладем новую ошибку с полем username а потом бы в контроллере проверили на ее наличие
    @Override
    public void validation(User user, Errors errors) {

        try {
            //если выбрасывается исключение то значит такого пользователя нет и это хорошо
            loadUserByUsername(user.getUsername());

            //и это тогда не будет работать
            errors.rejectValue("username", "", String.format("User with name %s exist yet", user.getUsername()));
        } catch (UsernameNotFoundException ignore) {}
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(findByUsername(username));
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user.get());
        //return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), user.getAuthorities());
    }
}

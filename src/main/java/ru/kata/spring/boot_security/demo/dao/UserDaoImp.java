package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Qualifier("users")
@Repository
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.merge(user);
    }

    @Override
    public void update(User newUser, long id) {
        User userOur = SearchUser(id);
        userOur.setUsername(newUser.getUsername());
        userOur.setLastname(newUser.getLastname());
        userOur.setEmail(newUser.getEmail());
        userOur.setAge(newUser.getAge());
        userOur.setPassword(newUser.getPassword());
        userOur.setRoles(newUser.getRoles());
    }

    @Override
    public List<User> findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("from User where username=:username", User.class);
        query.setParameter("username", username);
        List<User> user = query.getResultList();
        return user;
    }

    @Override
    public User SearchUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void remove(long id) {
        entityManager.remove(SearchUser(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getListUsers(String count) {
        List<User> list1 = (List<User>) entityManager.createQuery("from User").getResultList();
        list1 = list1.stream().sorted(Comparator.comparingLong(User::getId)).toList();
        if (count.matches("^-?\\d+$")) {
            int id = Integer.parseInt(count);
            if (id < 0) {
                return list1;
            }
            List<User> list2 = new ArrayList<>();
            User user = SearchUser(id);
            if (user == null) {
                return list2;
            }
            list2.add(user);
            return list2;
        }

        return list1.stream()
                .filter(x -> x.getUsername().toLowerCase().equals(count))
                .toList();

    }
}

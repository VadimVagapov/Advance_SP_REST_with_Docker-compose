package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "role")
    private String authority;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private List<User> userRole;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    public Role(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public List <User> getUserRole() {
        return userRole;
    }

    public void setUserRole(User user) {
        this.userRole = userRole;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = this.authority;
    }
    @Override
    public String getAuthority() {
        return authority;
    }

}

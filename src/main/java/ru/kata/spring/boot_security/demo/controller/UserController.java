package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class UserController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/admin_home")
    public String adminHome(Principal principal, Model model) {
        List<User> listUsers = userService.getListUsers("-1");
        model.addAttribute("listUsers", listUsers);
        User user = userService.findByUsername(principal.getName()).get(0);
        model.addAttribute("principal", user);
        User newUser = new User();
        model.addAttribute("new_user", newUser);
        Set<Role> roles = roleService.getAllRole();
        model.addAttribute("roles", roles);
        return "/admin/adminHome";
    }

    @GetMapping("/user")
    public String userHome(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).get(0);
        model.addAttribute("user", user);
        return "user";
    }


    @PostMapping(value = "/admin/new")
    public String saveUser(@ModelAttribute("user") @Validated User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "roles", required = false) long[] rolesId) {
        if (user.getEmail().isEmpty()
                && user.getUsername().isEmpty()
                && user.getLastname().isEmpty()
                && user.getPassword().isEmpty()) {
            return "redirect:/admin_home";
        }
        userService.validation(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/admin_home";
        }

        Set<Role> roles = new HashSet<>();

        if (rolesId == null) {
            roles.add(roleService.getRoleById(2));
        } else {
            for (long role : rolesId) {
                roles.add(roleService.getRoleById(role));
            }
        }
        user.setRoles(roles);
        userService.add(user);
        return "redirect:/admin_home";
    }

    @DeleteMapping(value = "/admin/delete{id}")
    public String deleteUserForm(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/admin_home";
    }

    @RequestMapping("/admin/edit")
    public String editUserForm(@RequestParam(name="id") long id, Model model) {
        User user = userService.SearchUser(id);
        Set<Role> roles = roleService.getAllRole();
        user.setRoles(roles);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "/admin/edit";
    }

    @PatchMapping(value = "/admin/adminHome{id}")
    public String updateUserForm(@ModelAttribute("user") User user, @PathVariable("id") long id,
                                 @RequestParam(value = "roles", required = false) long[] rolesId) {

        Set<Role> roles = new HashSet<>();
        if (rolesId == null) {
            User findUser = userService.findByUsername(user.getUsername()).get(0);
            roles = findUser.getRoles();
        } else {
            for (long role : rolesId) {
                roles.add(roleService.getRoleById(role));
            }
        }

        user.setRoles(roles);
        userService.update(user, id);
        return "redirect:/admin_home";
    }
}

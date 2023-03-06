package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String home(Model model) {
        List<User> listUsers = userService.getListUsers("-1");
        model.addAttribute("listUsers", listUsers);
        return "/admin/allUsers";
    }

    @GetMapping("/user")
    public String userHome(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).get(0);
        model.addAttribute("user", user);
        return "user";
    }


    @GetMapping("/admin/new")
    public String newUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        Set<Role> roles = userService.getDefaultRoles();
        model.addAttribute("roleList", roles);
        return "admin/new_user";
    }

    @PostMapping(value = "/admin/new")
    public String saveUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult) {
        if (user.getEmail().isEmpty()
                && user.getUsername().isEmpty()
                && user.getLastname().isEmpty()
                && user.getPassword().isEmpty()) {
            return "redirect:/admin";
        }
//        userService.validation(user, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "/admin/allUsers";
//        }
        userService.add(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public String deleteUserForm(@RequestParam(name="id") long id) {
        userService.remove(id);
        return "redirect:/admin";
    }

    @RequestMapping("/admin/edit")
    public String editUserForm(@RequestParam(name="id") long id, Model model) {
        User user = userService.SearchUser(id);
        model.addAttribute("user", user);
        Set<Role> roles = userService.getDefaultRoles();
        model.addAttribute("roleList", roles);
        return "/admin/edit";
    }

    @PatchMapping(value = "/admin/{id}")
    public String updateUserForm(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        userService.update(user, id);
        return "redirect:/admin";
    }

    @RequestMapping("/admin/search")
    public String getListUsers(@RequestParam(defaultValue = "0") String keyword, Model model) {
        if (keyword.equals("0")) {
            return "redirect:/admin";
        }
        model.addAttribute("users", userService.getListUsers(keyword));
        return "/admin/users";
    }
}

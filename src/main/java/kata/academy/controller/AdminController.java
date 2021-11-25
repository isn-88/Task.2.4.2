package kata.academy.controller;

import kata.academy.model.Role;
import kata.academy.model.User;
import kata.academy.repository.RoleRepository;
import kata.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UserService userService;


    @Autowired
    private RoleRepository roleRepository;


    @GetMapping()
    public String allUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "all-users";
    }


    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        user.setPassword("");
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "new-user";
    }


    @PostMapping()
    public String createNewUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new-user";
        }
        Set<Role> roles = user.getRoles();
        for (Role r : roles) {
            r.setId(roleRepository.findByRole(r.getRole()).getId());
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }


    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            return "update-user";
        }
        Set<Role> roles = user.getRoles();
        for (Role r : roles) {
            r.setId(roleRepository.findByRole(r.getRole()).getId());
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }


    @GetMapping("user-delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }


    @GetMapping("user-update/{id}")
    public String editUser(Model model, @PathVariable("id") long id) {
        User user = userService.findById(id);
        user.setPassword("");
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "update-user";
    }


    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }
}

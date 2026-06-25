package com.pharmacy.controller;

import com.pharmacy.entity.Role;
import com.pharmacy.entity.User;
import com.pharmacy.service.RoleService;
import com.pharmacy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute User user, BindingResult result,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "用户名已存在");
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }
        
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roleService.getRoleById(roleId).ifPresent(roles::add);
            }
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }
        return "redirect:/users";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute User user, BindingResult result,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }
        try {
            User existingUser = userService.getUserById(id).orElse(null);
            if (existingUser == null) {
                return "redirect:/users";
            }
            
            if (!existingUser.getUsername().equals(user.getUsername()) && userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "用户名已存在");
                model.addAttribute("roles", roleService.getAllRoles());
                return "users/form";
            }
            
            existingUser.setUsername(user.getUsername());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }
            existingUser.setRealName(user.getRealName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setEnabled(user.getEnabled());
            
            Set<Role> roles = new HashSet<>();
            if (roleIds != null) {
                for (Long roleId : roleIds) {
                    roleService.getRoleById(roleId).ifPresent(roles::add);
                }
            }
            existingUser.setRoles(roles);
            userService.saveUser(existingUser);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
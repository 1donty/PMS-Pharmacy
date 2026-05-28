package com.pharmacy.controller;

import com.pharmacy.entity.Role;
import com.pharmacy.entity.User;
import com.pharmacy.repository.RoleRepository;
import com.pharmacy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
        String error = request.getParameter("error");
        String logout = request.getParameter("logout");
        String registered = request.getParameter("registered");
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        if (logout != null) {
            model.addAttribute("loginLogout", true);
        }
        if (registered != null) {
            model.addAttribute("loginRegistered", true);
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String realName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            Model model) {

        // 验证密码是否一致
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }

        // 验证用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRealName(realName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setEnabled(true);

        // 为新用户分配普通用户角色
        Role userRole = roleRepository.findByCode("USER").orElse(null);
        if (userRole != null) {
            user.getRoles().add(userRole);
        }

        userRepository.save(user);

        // 注册成功后重定向到登录页面
        return "redirect:/login?registered";
    }
}
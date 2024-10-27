package org.nightfury.controller;

import lombok.AllArgsConstructor;
import org.nightfury.entity.User;
import org.nightfury.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.ROLE_USER);
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/user/profile")
    public String userProfile(
        @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
        Model model) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .orElse("ROLE_USER"));
        return "userProfile";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "adminDashboard";
    }
}


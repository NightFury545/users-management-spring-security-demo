package org.nightfury.controller;

import lombok.AllArgsConstructor;
import org.nightfury.entity.User;
import org.nightfury.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        @AuthenticationPrincipal Object principal,
        Model model) {

        String username;
        String role;

        if (principal instanceof org.springframework.security.core.userdetails.User user) {
            username = user.getUsername();
            role = user.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");
        } else {
            OAuth2User oAuth2User = (OAuth2User) principal;
            username = oAuth2User.getAttribute("name");
            role = oAuth2User.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");
        }

        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "userProfile";
    }


    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "adminDashboard";
    }
}


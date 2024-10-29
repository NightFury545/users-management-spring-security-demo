package org.nightfury.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.nightfury.entity.User;
import org.nightfury.entity.User.Role;
import org.nightfury.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@AllArgsConstructor
public class AuthenticationSuccessHandlerImpl implements
    AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        processOAuth2PostLogin(oAuth2User);
        response.sendRedirect("/user/profile");
    }

    private void processOAuth2PostLogin(OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("name");

        userRepository.findUserByUsername(name).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(name);
            newUser.setRole(Role.ROLE_USER);
            return userRepository.save(newUser);
        });
    }
}

package org.nightfury.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/**").permitAll())
            .formLogin(fl -> fl.loginPage("/login"))
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                .successHandler(authenticationSuccessHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            )
            .build();
    }
}

package org.nightfury;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nightfury.controller.UserController;
import org.nightfury.entity.User;
import org.nightfury.entity.User.Role;
import org.nightfury.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userService.save(any(User.class))).thenReturn(user);

        String view = userController.register(user);

        assertEquals("redirect:/login", view);
        verify(passwordEncoder).encode("password");
        verify(userService).save(any(User.class));
    }

    @Test
    void testUserProfileView() {
        User user = User.builder().username("testusername").password("testpassword")
            .role(Role.ROLE_USER).build();

        String view = userController.userProfile(
            (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()).password(user.getPassword()).authorities(user.getAuthorities()).build(), model);

        assertEquals("userProfile", view);
        verify(model).addAttribute("username", "testusername");
        verify(model).addAttribute("role", "USER");
    }

    void testAdminPanelAccessForRegularUser() {
        User user = User.builder().username("testusername").password("testpassword")
            .role(Role.ROLE_USER).build();

    }
}

package org.nightfury.service.impl;

import lombok.AllArgsConstructor;
import org.nightfury.entity.User;
import org.nightfury.repository.UserRepository;
import org.nightfury.service.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}

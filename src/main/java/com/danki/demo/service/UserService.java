package com.danki.demo.service;

import com.danki.demo.model.User;
import com.danki.demo.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
}

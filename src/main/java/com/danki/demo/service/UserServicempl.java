package com.danki.demo.service;

import com.danki.demo.model.Role;
import com.danki.demo.model.User;
import com.danki.demo.repository.UserRepository;
import com.danki.demo.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServicempl implements UserService{

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServicempl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
       User user = new User(registrationDto.getFirstName(),
               registrationDto.getLastName(),
               registrationDto.getEmail(),
               passwordEncoder.encode(registrationDto.getPassword()),
               Arrays.asList(new Role("ROLE_USER")));
       return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve a user from user repository
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password");
        }

        // Create a User provided by spring security
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}

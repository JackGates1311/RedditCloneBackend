package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.AuthTokenFilter;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    TokenUtils tokenUtils;

    @Override
    public void register(RegisterRequest registerRequest) {

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setAvatar(registerRequest.getAvatar());
        user.setRegistrationDate(LocalDateTime.now());
        user.setDescription(registerRequest.getDescription());
        user.setDisplayName(registerRequest.getDisplayName());

        userRepository.save(user);

    }

    @Override
    public User findByUsername(String username) {

        return null;
    }

}

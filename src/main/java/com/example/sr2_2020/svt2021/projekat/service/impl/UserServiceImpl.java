package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.ChangePasswordRequest;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.UserMapper;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PostRepository postRepository;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    AuthenticationManager authenticationManager;

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
    public ResponseEntity<ChangePasswordRequest> changePassword(ChangePasswordRequest changePasswordRequest,
                                                                HttpServletRequest request) {

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));


        Optional<User> userFound = userRepository.findByUsername(username);

        User user = userFound.orElseThrow(
                () -> new UsernameNotFoundException("User with entered username (" + username + ")  doesn't exists"));

        /// REFACTOR IT///

        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, changePasswordRequest.getOldPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticateUser);

        UserDetails userDetails = (UserDetails) authenticateUser.getPrincipal();

        String jwtToken = tokenUtils.generateToken(userDetails);

        /// DUPLICATE CODE /////

        if(!changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword()) &&
                jwtToken != null) {

            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(changePasswordRequest);

        } else {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(changePasswordRequest);
        }

    }

    @Override
    public UserInfoDTO getAccountInfo(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditCloneException(
                "User not found with username: " + username));

        int karma = postRepository.sumReactionCountByUser(user);

        return userMapper.mapUserInfoToDTO(user, karma);
    }

    @Override
    public ResponseEntity<?> updateAccountInfo(UserInfoDTO userInfoDTO, HttpServletRequest request) {

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "User with username " + username + " not found"));

        User userEditData = userMapper.mapDTOToUser(userInfoDTO);

        user.setAvatar(userEditData.getAvatar());
        user.setDisplayName(userEditData.getDisplayName());
        user.setDescription(userEditData.getDescription());

        userRepository.save(user);

        return new ResponseEntity<>("Account info has been succesfully changed", HttpStatus.ACCEPTED);

    }

}

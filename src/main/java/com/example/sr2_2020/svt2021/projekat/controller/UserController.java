package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.*;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import com.example.sr2_2020.svt2021.projekat.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){

        logger.info("LOGGER: " + LocalDateTime.now() + " - Register method has been called");

        userService.register(registerRequest);

        return new ResponseEntity<>("User registration successful", HttpStatus.OK);

    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Login method has been called");

        logger.info("LOGGER: " + LocalDateTime.now() + " - Authenticating user data ...");

        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticateUser);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting token data for specified user ...");

        UserDetails user = (UserDetails) authenticateUser.getPrincipal();

        String jwtToken = tokenUtils.generateToken(user);

        int expiresIn = tokenUtils.getExpiredIn();

        String role = tokenUtils.getUserRoleFromToken(jwtToken);

        //System.out.println("CLAIMS: " + tokenUtils.getClaimsFromToken(jwtToken));
        //System.out.println("ROLES: " + tokenUtils.getUserRoleFromToken(jwtToken));

        return ResponseEntity.ok(new AuthResponse(jwtToken, expiresIn, role));

    }

    
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public ResponseEntity<ChangePasswordRequest> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                          HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Change password method has been called");

        return userService.changePassword(changePasswordRequest, request);

    }

    
    @RequestMapping(value = "/accountInfo")
    public ResponseEntity<UserInfoDTO> getAccountInfo(HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get account info method has been called");

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        return new ResponseEntity<>(userService.getAccountInfo(username), HttpStatus.OK);
    }

    
    @RequestMapping(value = "/updateAccountInfo")
    public ResponseEntity<?> updateAccountInfo(@RequestBody UserInfoDTO userInfoDTO, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Update account info method has been called");

        return userService.updateAccountInfo(userInfoDTO, request);
    }

}

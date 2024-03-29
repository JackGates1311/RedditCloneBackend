package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.ChangePasswordRequest;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.UserMapper;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.CommentRepository;
import com.example.sr2_2020.svt2021.projekat.repository.FileRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PostRepository postRepository;

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private final CommentRepository commentRepository;

    private final FileRepository fileRepository;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public void register(RegisterRequest registerRequest) {

        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        //user.setAvatar(registerRequest.getAvatar());
        user.setRegistrationDate(LocalDateTime.now());
        user.setDescription(registerRequest.getDescription());
        user.setDisplayName(registerRequest.getDisplayName());
        user.setIsAdministrator(false);

        userRepository.save(user);

    }

    @Override
    public ResponseEntity<ChangePasswordRequest> changePassword(ChangePasswordRequest changePasswordRequest,
                                                                HttpServletRequest request) {

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));


        Optional<User> userFound = userRepository.findByUsername(username);

        User user = userFound.orElseThrow(
                () -> new UsernameNotFoundException("User with entered username (" + username + ")  doesn't exists"));

        ///TODO REFACTOR IT///

        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, changePasswordRequest.getOldPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticateUser);

        UserDetails userDetails = (UserDetails) authenticateUser.getPrincipal();

        String jwtToken = tokenUtils.generateToken(userDetails);

        ///TODO DUPLICATE CODE /////

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

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting account info ...");

        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditCloneException(
                "User not found with username: " + username));

        Integer postCount = postRepository.sumReactionCountByUser(user);
        Integer commentCount = commentRepository.sumReactionCountByUser(user);

        if(postCount == null)
            postCount = 0;

        if(commentCount == null)
            commentCount = 0;

        int karma = postCount + commentCount;

        String avatar = null;

        if(!Objects.isNull(fileRepository.findByUser(user)))
            avatar = fileRepository.findByUser(user).getFilename();

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving to database ...");

        return userMapper.mapUserInfoToDTO(user, karma, avatar);
    }

    @Override
    public ResponseEntity<?> updateAccountInfo(UserInfoDTO userInfoDTO, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Updating data ...");

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "User with username " + username + " not found"));

        User userEditData = userMapper.mapDTOToUser(userInfoDTO);

        //TODO does it is really needed?
        //user.setAvatar(userEditData.getAvatar());
        user.setDisplayName(userEditData.getDisplayName());
        user.setDescription(userEditData.getDescription());

        userRepository.save(user);

        return new ResponseEntity<>("Account info has been successfully changed", HttpStatus.ACCEPTED);

    }

}

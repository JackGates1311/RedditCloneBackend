package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.UserMapper;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest.RegisterRequestBuilder;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO.UserInfoDTOBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User.UserBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapperImpl implements UserMapper {

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public RegisterRequest mapRegisterRequestToDTO(User user) {

        if (user == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - User object is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new register response ...");

        RegisterRequestBuilder registerRequest = RegisterRequest.builder();

        registerRequest.username(user.getUsername());
        registerRequest.password(user.getPassword());
        registerRequest.email(user.getEmail());
        registerRequest.avatar(user.getAvatar());
        registerRequest.description(user.getDescription());
        registerRequest.displayName(user.getDisplayName());

        return registerRequest.build();
    }

    @Override
    public User mapDTOToUser(RegisterRequest registerRequest) {

        if(registerRequest == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - registerRequest body is null ...");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new user ...");

        UserBuilder user = User.builder();

        user.username(registerRequest.getUsername());
        user.password(registerRequest.getPassword());
        user.email(registerRequest.getEmail());
        user.avatar(registerRequest.getAvatar());
        user.description(registerRequest.getDescription());
        user.displayName(registerRequest.getDisplayName());

        logger.info("LOGGER: " + LocalDateTime.now() + " - New user has been successfully mapped to object");

        return user.build();
    }

    @Override
    public UserInfoDTO mapUserInfoToDTO(User user, int karma) {

        if(user == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - User object is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new user response ...");

        UserInfoDTOBuilder userInfoDTO = UserInfoDTO.builder();

        userInfoDTO.username(user.getUsername());
        userInfoDTO.email(user.getEmail());
        userInfoDTO.avatar(user.getAvatar());
        userInfoDTO.displayName(user.getDisplayName());
        userInfoDTO.description(user.getDescription());
        userInfoDTO.karma(karma);

        logger.info("LOGGER: " + LocalDateTime.now() + " - User info has been successfully mapped to object");

        return userInfoDTO.build();
    }

    @Override
    public User mapDTOToUser(UserInfoDTO userInfoDTO) {

        if(userInfoDTO == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - userInfoDTO body is null ...");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new user ...");

        UserBuilder user = User.builder();

        user.username(userInfoDTO.getUsername());
        user.email(userInfoDTO.getEmail());
        user.avatar(userInfoDTO.getAvatar());
        user.description(userInfoDTO.getDescription());
        user.displayName(userInfoDTO.getDisplayName());

        logger.info("LOGGER: " + LocalDateTime.now() + " - User has been successfully mapped to object");

        return user.build();
    }


}

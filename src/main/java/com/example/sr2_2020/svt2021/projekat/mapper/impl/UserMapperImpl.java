package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.UserMapper;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest.RegisterRequestBuilder;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO.UserInfoDTOBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User.UserBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public RegisterRequest mapRegisterRequestToDTO(User user) {

        if (user == null)
            return null;

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

        if(registerRequest == null)
            return null;

        UserBuilder user = User.builder();

        user.username(registerRequest.getUsername());
        user.password(registerRequest.getPassword());
        user.email(registerRequest.getEmail());
        user.avatar(registerRequest.getAvatar());
        user.description(registerRequest.getDescription());
        user.displayName(registerRequest.getDisplayName());

        return user.build();
    }

    @Override
    public UserInfoDTO mapUserInfoToDTO(User user, int karma) {

        if(user == null)
            return null;

        UserInfoDTOBuilder userInfoDTO = UserInfoDTO.builder();

        userInfoDTO.username(user.getUsername());
        userInfoDTO.email(user.getEmail());
        userInfoDTO.avatar(user.getAvatar());
        userInfoDTO.displayName(user.getDisplayName());
        userInfoDTO.description(user.getDescription());
        userInfoDTO.karma(karma);

        return userInfoDTO.build();
    }

    @Override
    public User mapDTOToUser(UserInfoDTO userInfoDTO) {

        if(userInfoDTO == null)
            return null;

        UserBuilder user = User.builder();

        user.username(userInfoDTO.getUsername());
        user.email(userInfoDTO.getEmail());
        user.avatar(userInfoDTO.getAvatar());
        user.description(userInfoDTO.getDescription());
        user.displayName(userInfoDTO.getDisplayName());

        return user.build();
    }


}

package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public RegisterRequest mapRegisterRequestToDTO(User user);

    public User mapDTOToUser (RegisterRequest registerRequest);

    public UserInfoDTO mapUserInfoToDTO(User user, int karma);

    public User mapDTOToUser (UserInfoDTO userInfoDTO);
}

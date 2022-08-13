package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    RegisterRequest mapRegisterRequestToDTO(User user);

    User mapDTOToUser(RegisterRequest registerRequest);

    UserInfoDTO mapUserInfoToDTO(User user, int karma, String avatar);

    User mapDTOToUser (UserInfoDTO userInfoDTO);

}

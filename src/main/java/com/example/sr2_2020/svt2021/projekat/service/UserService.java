package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.ChangePasswordRequest;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.dto.UserInfoDTO;
import com.example.sr2_2020.svt2021.projekat.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


public interface UserService {

    @Transactional
    public void register(RegisterRequest registerRequest);

    @Transactional
    public ResponseEntity<ChangePasswordRequest> changePassword(ChangePasswordRequest changePasswordRequest,
                                                                HttpServletRequest request);
    @Transactional
    public UserInfoDTO getAccountInfo(String username);

    @Transactional
    public ResponseEntity<?> updateAccountInfo(UserInfoDTO userInfoDTO, HttpServletRequest request);
}

package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.AuthResponse;
import com.example.sr2_2020.svt2021.projekat.dto.LoginRequest;
import com.example.sr2_2020.svt2021.projekat.dto.RegisterRequest;
import com.example.sr2_2020.svt2021.projekat.model.User;

import javax.transaction.Transactional;

public interface UserService {

    @Transactional
    public void register(RegisterRequest registerRequest);

    @Transactional
    public User login(LoginRequest loginRequest);

    public User findByUsername(String username);
}

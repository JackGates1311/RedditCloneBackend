package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String avatar;
    private String description;
    private String displayName;
    private Boolean isAdministrator;
}

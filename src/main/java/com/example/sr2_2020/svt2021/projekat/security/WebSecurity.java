package com.example.sr2_2020.svt2021.projekat.security;

import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebSecurity {

    @Autowired
    private UserService userService;

    public boolean checkUserId(Authentication authentication, HttpServletRequest request, int id) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByUsername(userDetails.getUsername());

        if(id == user.getUserId()) {

            return true;
        }

        return false;
    }

}

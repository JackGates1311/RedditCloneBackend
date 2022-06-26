package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userFound = userRepository.findByUsername(username);

        User user = userFound.orElseThrow(
                () -> new UsernameNotFoundException("User with entered username (" + username + ")  doesn't exists"));

        String role = "USER";

        if(user.getIsAdministrator())
            role = "ADMINISTRATOR";


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
                true, true, true, getAuthorities(role));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {

        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

}

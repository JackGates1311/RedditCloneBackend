package com.example.sr2_2020.svt2021.projekat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter { // FIX CLASS

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EncoderConfig encoderConfig;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService).passwordEncoder(encoderConfig.passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        return super.authenticationManagerBean();
    }

    @Bean
    public AuthTokenFilter authTokenFilterBean()
            throws Exception {

        AuthTokenFilter authTokenFilter = new AuthTokenFilter();

        authTokenFilter.setAuthManager(authenticationManagerBean());

        return authTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.headers().cacheControl().disable();

        httpSecurity.cors();

        httpSecurity.headers().frameOptions().disable();

        // In line below, we can exclude some requests from auth (enables entered links for guest)

        httpSecurity.csrf().disable().authorizeHttpRequests().
                antMatchers("/api/auth/**", "/api/posts/getAllPosts", "/api/communities/name={name}",
                        "/api/posts/communityName={communityName}", "/api/communities/getAllCommunities",
                        "/api/posts/{id}").permitAll().anyRequest().authenticated();

        httpSecurity.addFilterBefore(authTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

}

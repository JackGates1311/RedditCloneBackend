package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.mapper.PostMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.security.AuthTokenFilter;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.PostService;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    PostMapper postMapper;

    @Override
    public void save(PostRequest postRequest, HttpServletRequest request) {

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        postRepository.save(postMapper.map(postRequest, username));

    }

    @Override
    public List<PostResponse> getAllPosts() {

        return postRepository.findAll().stream().map(postMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PostResponse getPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        return postMapper.mapToDTO(post);
    }
}

package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.exception.CommunityNotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        Community community = communityRepository.findByName(postRequest.getCommunityName()).
                orElseThrow(() -> new CommunityNotFoundException("Community with name " + postRequest.getCommunityName()
                        + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        postRepository.save(postMapper.map(postRequest, community, username));

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

    @Override
    public ResponseEntity<PostRequest> editPost(PostRequest postRequest, Long id, HttpServletRequest request) {

        Community community = communityRepository.findByName(postRequest.getCommunityName()).
                orElseThrow(() -> new CommunityNotFoundException("Community with name " + postRequest.getCommunityName()
                        + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        Post post = postMapper.map(postRequest, community, username);

        post.setPostId(id);

        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(postRequest);
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {

        postRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

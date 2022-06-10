package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

public interface PostService {

    @Transactional
    public void save(PostRequest postRequest, HttpServletRequest request);

    @Transactional
    public List<PostResponse> getAllPosts();

    @Transactional
    public PostResponse getPost(Long id);

    @Transactional
    public ResponseEntity<PostRequest> editPost(PostRequest postRequest, Long id, HttpServletRequest request);

    @Transactional
    public ResponseEntity<?> deleteById(Long id, HttpServletRequest request);

    @Transactional
    public List<PostResponse> getPostsByCommunityName(String communityName);
}

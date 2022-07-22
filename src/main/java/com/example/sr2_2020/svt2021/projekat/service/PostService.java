package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface PostService {

    @Transactional
    void save(PostRequest postRequest, HttpServletRequest request);

    @Transactional
    List<PostResponse> getAllPosts();

    @Transactional
    PostResponse getPost(Long id);

    @Transactional
    ResponseEntity<PostRequest> editPost(PostRequest postRequest, Long id, HttpServletRequest request);

    @Transactional
    ResponseEntity<?> deleteById(Long id, HttpServletRequest request);

    @Transactional
    List<PostResponse> getPostsByCommunityName(String communityName);
}

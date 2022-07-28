package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    static final Logger logger = LogManager.getLogger(CommunityController.class);
    
    @RequestMapping(value = "/createPost", method = RequestMethod.POST)
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Create post method has been called");

        postService.save(postRequest, request);

        return ResponseEntity.status(HttpStatus.CREATED).body("Post is successfully created");
    }

    @RequestMapping(value = "/getAllPosts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get all posts method has been called");

        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get post method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PostRequest> editPost(@RequestBody PostRequest postRequest, @PathVariable Long id,
            HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Edit post method has been called");

        return postService.editPost(postRequest, id, request);
    }

    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Delete post method has been called");

        return postService.deleteById(id, request);
    }

    @RequestMapping("/communityName={communityName}")
    public ResponseEntity<List<PostResponse>> getCommunityPosts(@PathVariable String communityName) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get posts by community method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByCommunityName(communityName));
    }

}

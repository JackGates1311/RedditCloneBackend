package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.model.PostSearching;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.PdfService;
import com.example.sr2_2020.svt2021.projekat.elasticsearch.services.PostSearchingService;
import com.example.sr2_2020.svt2021.projekat.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    private final PostSearchingService postSearchingService;

    private final PdfService pdfService;

    static final Logger logger = LogManager.getLogger(CommunityController.class);
    
    @RequestMapping(value = "/createPost", method = RequestMethod.POST)
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest, HttpServletRequest request) {

        //TODO Implement file upload within post creation

        logger.info("LOGGER: " + LocalDateTime.now() + " - Create post method has been called");

        return postService.save(postRequest, request);
    }

    @RequestMapping(value = "/getAllPosts")
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam(required = false) String sortBy) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get all posts method has been called");

        return new ResponseEntity<>(postService.getAllPosts(sortBy), HttpStatus.OK);
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
    public ResponseEntity<List<PostResponse>> getCommunityPosts(@PathVariable String communityName,
        @RequestParam(required = false) String sortBy) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get posts by community method has been called");

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByCommunityName(communityName, sortBy));
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<PostSearching>> searchPosts(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "minKarma", required = false) Integer minKarma,
            @RequestParam(value = "maxKarma", required = false) Integer maxKarma,
            @RequestParam(value = "minComments", required = false) Float minComments,
            @RequestParam(value = "maxComments", required = false) Float maxComments,
            @RequestParam(value = "flairs", required = false) String flairs,
            @RequestParam(value = "isMust", required = false) Boolean isMust,
            @RequestParam(value = "isPdfIndex", required = false) Boolean isPdfIndex,
            @RequestParam(value = "titleSearchMode", required = false) String titleSearchMode,
            @RequestParam(value = "textSearchMode", required = false) String textSearchMode,
            @RequestParam(value = "flairsSearchMode", required = false) String flairsSearchMode
    ) {

        if(isMust == null)
            isMust = true;
        if(isPdfIndex == null)
            isPdfIndex = false;

        return postSearchingService.searchPosts(title, text, minKarma, maxKarma, minComments, maxComments,
                flairs, isMust, isPdfIndex, titleSearchMode, textSearchMode, flairsSearchMode);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<List<PostSearching>> searchPostsByPdfDocument(
            @RequestParam("file") MultipartFile pdfFile,
            @RequestParam(value = "isPdfIndex", required = false) Boolean isPdfIndex
    ) {

        String text = postSearchingService.getPdfText(pdfService.getPdfContent(pdfFile));

        logger.info("Text successfully caught from PDF document: " + text);

        if(isPdfIndex == null)
            isPdfIndex = true;

        return postSearchingService.searchPosts(null, text, null, null, null,
                null, null, true, isPdfIndex, null, null,
                null);
    }
}

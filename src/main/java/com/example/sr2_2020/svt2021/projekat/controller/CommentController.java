package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import com.example.sr2_2020.svt2021.projekat.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Slf4j
public class CommentController {

    static final Logger logger = LogManager.getLogger(CommentController.class);

    private final CommentService commentService;

    @RequestMapping(value = "/postComment", method =  RequestMethod.POST)
    public ResponseEntity<String> postComment(@RequestBody CommentDTORequest commentDTO, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Post comment method has been called");

        commentService.save(commentDTO, request);

        return ResponseEntity.status(HttpStatus.OK).body("Comment is successfully posted");
    }

    @RequestMapping(value = "/getPostComments/{id}")
    public ResponseEntity<List<CommentDTOResponse>> getPostComments (@PathVariable Long id,
        @RequestParam(required = false) String sortBy, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get post comments method has been called");

        return new ResponseEntity<>(commentService.getPostComments(id, sortBy, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<CommentDTOResponse> getComment(@PathVariable Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Get comment method has been called");

        return new ResponseEntity<>(commentService.getComment(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> editComment(@PathVariable Long id, @RequestBody CommentDTORequest
            commentDTORequest, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Edit comment method has been called");

        return commentService.editComment(commentDTORequest, id, request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteComment(@PathVariable Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Delete comment method has been called");

        return commentService.deleteComment(id, request);
    }


}

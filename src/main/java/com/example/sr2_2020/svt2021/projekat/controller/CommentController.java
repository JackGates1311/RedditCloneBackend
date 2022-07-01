package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import com.example.sr2_2020.svt2021.projekat.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Slf4j
public class CommentController {

    @Autowired
    CommentService commentService;

    
    @RequestMapping(value = "/postComment", method =  RequestMethod.POST)
    public ResponseEntity postComment(@RequestBody CommentDTORequest commentDTO, HttpServletRequest request) {

        commentService.save(commentDTO, request);

        return new ResponseEntity("Comment is successfully posted", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getPostComments/{id}")
    public ResponseEntity<List<CommentDTOResponse>> getPostComments (@PathVariable Long id, HttpServletRequest request)
    {

        return new ResponseEntity<>(commentService.getPostComments(id, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<CommentDTOResponse> getComment(@PathVariable Long id) {

        return new ResponseEntity<CommentDTOResponse>(commentService.getComment(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CommentDTORequest> editComment(@PathVariable Long id, @RequestBody CommentDTORequest
            commentDTORequest, HttpServletRequest request) {

        return commentService.editComment(commentDTORequest, id, request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteComment(@PathVariable Long id, HttpServletRequest request) {

        return commentService.deleteComment(id, request);
    }


}

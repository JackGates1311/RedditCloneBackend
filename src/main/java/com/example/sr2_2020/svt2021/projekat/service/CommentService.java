package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {

    public void save(CommentDTORequest commentDTO, HttpServletRequest request);

    public List<CommentDTOResponse> getPostComments(Long id, HttpServletRequest request);

    public ResponseEntity<CommentDTORequest> editComment(CommentDTORequest commentDTORequest, Long id, HttpServletRequest
            request);

    public ResponseEntity deleteComment(Long id, HttpServletRequest request);
}

package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface CommentService {

    @Transactional
    public void save(CommentDTORequest commentDTO, HttpServletRequest request);

    @Transactional
    public List<CommentDTOResponse> getPostComments(Long id, HttpServletRequest request);

    @Transactional
    public ResponseEntity<CommentDTORequest> editComment(CommentDTORequest commentDTORequest, Long id, HttpServletRequest
            request);

    @Transactional
    public ResponseEntity deleteComment(Long id, HttpServletRequest request);

    @Transactional
    public CommentDTOResponse getComment(Long id);
}

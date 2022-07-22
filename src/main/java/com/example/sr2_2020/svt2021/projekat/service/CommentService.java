package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface CommentService {

    @Transactional
    void save(CommentDTORequest commentDTO, HttpServletRequest request);

    @Transactional
    List<CommentDTOResponse> getPostComments(Long id, HttpServletRequest request);

    @Transactional
    ResponseEntity<String> editComment(CommentDTORequest commentDTORequest, Long id, HttpServletRequest
            request);

    @Transactional
    ResponseEntity<String> deleteComment(Long id, HttpServletRequest request);

    @Transactional
    CommentDTOResponse getComment(Long id);
}

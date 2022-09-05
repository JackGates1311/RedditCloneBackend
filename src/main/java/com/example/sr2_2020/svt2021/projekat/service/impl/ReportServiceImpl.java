package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.ReportDTO;
import com.example.sr2_2020.svt2021.projekat.exception.CommentNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.mapper.ReportMapper;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Report;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.CommentRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.ReportRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final TokenUtils tokenUtils;
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    @Override
    public ResponseEntity<String> report(ReportDTO reportDTO, HttpServletRequest request) {

        //TODO check this method if it works correctly, description can be optional in any case!

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username: " + username));

        Post post = null;

        Comment comment = null;

        Optional<Report> reportsByObjectAndUser;

        if(reportDTO.getPostId() != null) {

            //TODO implement for saving post report

            post = postRepository.findById(reportDTO.getPostId()).orElseThrow(() ->
                    new PostNotFoundException("Post with id " + reportDTO.getPostId() + " not found"));

            reportsByObjectAndUser = reportRepository.findByPostAndUser(post, user);

        } else {

            comment = commentRepository.findById(reportDTO.getCommentId()).orElseThrow(() ->
                    new CommentNotFoundException("Comment with id " + reportDTO.getCommentId() + " not found"));

            reportsByObjectAndUser = reportRepository.findByCommentAndUser(comment, user);

        }

        if(reportsByObjectAndUser.isPresent()){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have been already reported this post");

        } else {

            reportRepository.save(reportMapper.mapDTOToReport(reportDTO, post, user, comment));

        }

        return ResponseEntity.status(HttpStatus.OK).body("Your report has been processed successfully");
    }
}

package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import com.example.sr2_2020.svt2021.projekat.exception.CommentNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommentMapper;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.CommentRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentMapper commentMapper;

    @Override
    public void save(CommentDTORequest commentDTORequest, HttpServletRequest request) {

        Post post = postRepository.findById(commentDTORequest.getPostId()).orElseThrow(() ->
                new PostNotFoundException("Post not found for specified post id"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        if(commentDTORequest.getRepliedToCommentId() != null) {

            Comment comment = commentRepository.findById(commentDTORequest.getRepliedToCommentId()).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            Comment newComment = commentRepository.save(commentMapper.mapDTOToComment(commentDTORequest, post,
                    user));

            if(Objects.equals(comment.getReplies(), "")) {

                comment.setReplies(newComment.getCommentId().toString());

            } else {

                comment.setReplies(comment.getReplies() + "," + newComment.getCommentId().toString());
            }

            commentRepository.save(comment);

        } else {

            commentRepository.save(commentMapper.mapDTOToComment(commentDTORequest, post, user));
        }




    }

    @Override
    public List<CommentDTOResponse> getPostComments(Long id, HttpServletRequest request) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(
                "Post not found for ID: " + id));

        return commentRepository.findAllByPost(post).stream().map((Comment comment) ->
                commentMapper.mapCommentToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity editComment(CommentDTORequest commentDTORequest, Long id,
                                      HttpServletRequest request) {

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        Comment commentForEdit = commentRepository.findById(id).orElseThrow(() -> new
                CommentNotFoundException("Comment not found with specified commentId"));

        if(Objects.equals(commentForEdit.getUser().getUsername(), username)) {

            commentForEdit.setCommentId(id);
            commentForEdit.setText(commentDTORequest.getText());

            commentRepository.save(commentForEdit);

            return new ResponseEntity("Comment has been successfully edited", HttpStatus.ACCEPTED);

        }

        return new ResponseEntity("You don't have permissions to edit this comment", HttpStatus.FORBIDDEN);
    }

    List<Long> commentIdsToDelete;

    @Override
    public ResponseEntity deleteComment(Long id, HttpServletRequest request) {

        commentIdsToDelete.clear();

        return getCommentsToDelete(id, request);
    }

    private ResponseEntity getCommentsToDelete(Long id, HttpServletRequest request) {

        boolean isFinished = false;
        boolean isParentComment = true;

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        while(!isFinished) {

            Comment parentComment = commentRepository.findById(id).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            if(!Objects.equals(parentComment.getUser().getUsername(), username) && isParentComment) {

                return new ResponseEntity("You don't have permissions to delete this comment",
                        HttpStatus.FORBIDDEN);
            }

            isParentComment = false;

            commentIdsToDelete.add(parentComment.getCommentId());

            List<String> parentCommentIdReplies = List.of(parentComment.getReplies().split(","));

            if(parentCommentIdReplies.get(0).equals("")) {

                isFinished = true;

            } else {

                Comment childComment;

                int i = 0;

                while (i < parentCommentIdReplies.size()) {

                    childComment = commentRepository.findById(Long.parseLong(
                            parentCommentIdReplies.get(i))).orElseThrow(() ->
                            new CommentNotFoundException("Comment not found with specified commentId"));

                    commentIdsToDelete.add(Long.valueOf(parentCommentIdReplies.get(i)));

                    i++;

                    id = childComment.getCommentId();

                    getCommentsToDelete(id, request);

                }


            }

        }

        commentIdsToDelete = commentIdsToDelete.stream().distinct().collect(Collectors.toList());

        System.out.println("AFTER:" + commentIdsToDelete);

        deleteComments(commentIdsToDelete);

        return new ResponseEntity("Comment has been successfully deleted", HttpStatus.ACCEPTED);
    }

    private void deleteComments(List<Long> commentIdsToDelete) {

        for (Long commentId : commentIdsToDelete) {

            Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            comment.setIsDeleted(true);

            commentRepository.save(comment);
        }
    }
}

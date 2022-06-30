package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import com.example.sr2_2020.svt2021.projekat.mapper.CommentMapper;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.model.Comment.CommentBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment mapDTOToComment(CommentDTORequest commentDTO, Post post, User user) {

        if(commentDTO == null)
            return null;

        CommentBuilder commentBuilder = Comment.builder();

        commentBuilder.commentId(commentDTO.getCommentId());
        commentBuilder.text(commentDTO.getText());
        commentBuilder.timestamp(LocalDateTime.now());
        commentBuilder.isDeleted(false);
        commentBuilder.replies("");
        commentBuilder.post(post);
        commentBuilder.user(user);
        commentBuilder.reactionCount(0);


        return commentBuilder.build();
    }

    @Override
    public CommentDTOResponse mapCommentToDTO(Comment comment, List<CommentDTOResponse> replies) {

        if(comment == null)
            return null;

        CommentDTOResponse commentDTO = new CommentDTOResponse();

        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setReplies(replies);
        commentDTO.setText(comment.getText());
        commentDTO.setTimestamp(comment.getTimestamp().toString());
        commentDTO.setPostId(comment.getPost().getPostId());
        commentDTO.setUserId(comment.getUser().getUserId());
        commentDTO.setIsDeleted(false);
        commentDTO.setUsername(comment.getUser().getUsername());
        commentDTO.setReactionCount(comment.getReactionCount());

        return commentDTO;
    }
}

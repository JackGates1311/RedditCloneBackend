package com.example.sr2_2020.svt2021.projekat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTORequest {

    private Long commentId;
    private String text;
    private Long postId;
    private Long repliedToCommentId;

}

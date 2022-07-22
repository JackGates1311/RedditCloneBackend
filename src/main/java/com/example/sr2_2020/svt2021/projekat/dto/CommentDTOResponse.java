package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTOResponse {

    private Long commentId;
    private String text;
    private String timestamp;
    private Long postId;
    private Long userId;
    private String username;
    private List<CommentDTOResponse> replies;
    private Boolean isDeleted;
    private Integer reactionCount;

}

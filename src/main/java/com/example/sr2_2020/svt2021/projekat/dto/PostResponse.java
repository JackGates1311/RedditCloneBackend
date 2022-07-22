package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private String communityName;
    private String creationDate;
    private String imagePath;
    private String text;
    private String title;
    private String username;
    private Integer reactionCount;
    private Integer commentCount;

}

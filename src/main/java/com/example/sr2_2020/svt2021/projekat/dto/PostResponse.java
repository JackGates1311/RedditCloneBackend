package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private String communityName;
    private String creationDate;
    private String text;
    private String title;
    private String username;
    private Integer reactionCount;
    private Integer commentCount;
    private List<String> images;
    private List<String> flairs;

}

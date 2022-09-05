package com.example.sr2_2020.svt2021.projekat.dto;

import com.example.sr2_2020.svt2021.projekat.model.Flair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    //TODO merge DTO Response and DTO Request to one big DTO (for all DTOs)...

    private Long postId;
    private String communityName;
    private String creationDate;
    private String text;
    private String title;
    private Integer reactionCount;
    private List<String> flairs;

}

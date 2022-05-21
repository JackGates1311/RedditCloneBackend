package com.example.sr2_2020.svt2021.projekat.dto;

import com.example.sr2_2020.svt2021.projekat.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private Long postId;
    private LocalDateTime creationDate;
    private String communityName;
    private String imagePath;
    private String text;
    private String title;
   // private String username;

}

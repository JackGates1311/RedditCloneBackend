package com.example.sr2_2020.svt2021.projekat.dto;


import com.example.sr2_2020.svt2021.projekat.model.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDTO {

    private long reactionId;
    private ReactionType reactionType;
    private Long postId;
    private String username;

}

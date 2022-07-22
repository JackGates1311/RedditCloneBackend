package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityDTO {

    private Long communityId;
    private String name;
    private String description;
    private String creationDate;
    private Boolean isSuspended;
    private String suspendedReason;
    private int numberOfPosts;

}

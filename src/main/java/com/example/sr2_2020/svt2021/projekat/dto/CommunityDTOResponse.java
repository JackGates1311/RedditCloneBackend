package com.example.sr2_2020.svt2021.projekat.dto;

import com.example.sr2_2020.svt2021.projekat.model.Flair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityDTOResponse {

    private Long communityId;
    private String name;
    private String description;
    private String creationDate;
    private Boolean isSuspended;
    private String suspendedReason;
    private int numberOfPosts;
    private List<String> flairs;

}

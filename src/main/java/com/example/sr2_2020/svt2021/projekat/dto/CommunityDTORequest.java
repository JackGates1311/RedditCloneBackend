package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityDTORequest {
    private Long communityId;
    private String name;
    private String description;
    private Boolean isSuspended;
    private String suspendedReason;
    private List<String> flairs;
}

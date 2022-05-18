package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityDTO {

    private Long communityId;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private Boolean isSuspended;
    private String suspendedReason;


}

package com.example.sr2_2020.svt2021.projekat.dto;

import com.example.sr2_2020.svt2021.projekat.model.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {

    private Long reportId;
    private ReportReason reportReason;
    private String reportDescription;
    private Boolean accepted;
    private Long userId;
    private Long postId;
    private Long commentId;

}

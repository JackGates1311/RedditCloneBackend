package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.ReportDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.ReportMapper;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Report;
import com.example.sr2_2020.svt2021.projekat.model.Report.ReportBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReportMapperImpl implements ReportMapper {
    @Override
    public Report mapDTOToReport(ReportDTO reportDTO, Post post, User user, Comment comment) {

        if(reportDTO == null) {
            return null;
        }

        ReportBuilder report = Report.builder();

        report.reportId(reportDTO.getReportId());
        report.reportReason(reportDTO.getReportReason());
        report.reportDescription(reportDTO.getReportDescription());
        report.timestamp(LocalDateTime.now());
        report.accepted(reportDTO.getAccepted());
        report.user(user);
        report.post(post);
        report.comment(comment);

        return report.build();
    }

    @Override
    public ReportDTO mapReportToDTO(Report report) {

        if(report == null)
            return null;

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setReportId(report.getReportId());
        reportDTO.setReportReason(report.getReportReason());
        reportDTO.setReportDescription(report.getReportDescription());
        reportDTO.setAccepted(report.getAccepted());
        reportDTO.setUserId(report.getUser().getUserId());
        try{
            reportDTO.setPostId(report.getPost().getPostId());
        } catch (Exception ignored) {
            reportDTO.setCommentId(report.getComment().getCommentId());
        }

        return reportDTO;
    }
}

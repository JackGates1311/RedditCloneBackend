package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.ReportDTO;
import com.example.sr2_2020.svt2021.projekat.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    Report mapDTOToReport (ReportDTO reportDTO, Post post, User user, Comment comment);

    ReportDTO mapReportToDTO(Report report);
}

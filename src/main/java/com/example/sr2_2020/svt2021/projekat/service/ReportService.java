package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.ReportDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ReportService {
    ResponseEntity<String> report(ReportDTO reportDTO, HttpServletRequest request);
}

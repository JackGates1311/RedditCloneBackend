package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.ReportDTO;
import com.example.sr2_2020.svt2021.projekat.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> report(@RequestBody ReportDTO reportDTO, HttpServletRequest request) {

        return reportService.report(reportDTO, request);
    }

    @RequestMapping("/{communityId}")
    public ResponseEntity<List<ReportDTO>> getAllReportsByCommunity(@PathVariable Long communityId){

        return null;
    }
}

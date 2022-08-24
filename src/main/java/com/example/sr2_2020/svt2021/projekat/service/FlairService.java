package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.FlairDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FlairService {

    ResponseEntity<String> save(FlairDTO flairDTO);
    List<FlairDTO> getAllFlairs();
    ResponseEntity<FlairDTO> getFlair(Long id);
    ResponseEntity<?> editFlair(FlairDTO flairDTO, String name);
    ResponseEntity<String> deleteFlair(String name);
}

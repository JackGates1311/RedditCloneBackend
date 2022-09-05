package com.example.sr2_2020.svt2021.projekat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {

    private String fileNames;
    private String message;
}

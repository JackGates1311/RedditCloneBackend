package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface FileService {

    ResponseEntity<FileResponse> uploadFile(String path, MultipartFile[] multipartFiles, Optional<Long> postIdForSave,
                                            HttpServletRequest request) throws IOException;

    InputStream getFile(String path, String filename) throws FileNotFoundException;
}

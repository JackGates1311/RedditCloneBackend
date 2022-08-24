package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.FileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface FileService {

    @Transactional
    ResponseEntity<FileResponse> uploadFile(String path, MultipartFile[] multipartFiles, Long postId,
                                            HttpServletRequest request) throws IOException;
    @Transactional
    InputStream getFile(String path, String filename) throws FileNotFoundException;

    @Transactional
    ResponseEntity<FileResponse> replaceFile(String savePath, MultipartFile[] multipartFiles,
                                             HttpServletRequest request) throws IOException;
    @Transactional
    ResponseEntity<FileResponse> deleteFile(String savePath, String filename) throws IOException;
}

package com.example.sr2_2020.svt2021.projekat.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    List<String> uploadFile(String path, MultipartFile[] multipartFiles) throws IOException;

    InputStream getFile(String path, String filename) throws FileNotFoundException;
}

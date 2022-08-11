package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<String> uploadFile(String savePath, MultipartFile[] multipartFiles) throws IOException {

        List<String> fileNames = new ArrayList<>();

        File path = new File(savePath);

        boolean isDirectoryCreated = path.exists();

        if (!isDirectoryCreated) {

            isDirectoryCreated = path.mkdir();
        }

        if(isDirectoryCreated) {

            for(MultipartFile multipartFile : multipartFiles){

                String filename = multipartFile.getOriginalFilename();

                String randomID = UUID.randomUUID().toString();

                assert filename != null;
                String generatedFileName = randomID + "_" + filename.trim();

                String filePath = savePath + File.separator + generatedFileName;

                Files.copy(multipartFile.getInputStream(), Paths.get(filePath));

                fileNames.add(filename);

            }

        }

        return fileNames;

    }

    @Override
    public InputStream getFile(String path, String filename) throws FileNotFoundException {

        String fullPath = path + File.separator + filename;

        return new FileInputStream(fullPath);
    }
}

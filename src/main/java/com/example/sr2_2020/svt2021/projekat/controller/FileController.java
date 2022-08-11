package com.example.sr2_2020.svt2021.projekat.controller;

import com.example.sr2_2020.svt2021.projekat.dto.FileResponse;
import com.example.sr2_2020.svt2021.projekat.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;

    @Value("${project.file.path.save.location}")
    private String savePath;

    public FileController(FileService fileService) {

        this.fileService = fileService;
    }

    @RequestMapping (value="/upload", method = RequestMethod.POST)
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("files") MultipartFile[] multipartFiles)
            throws IOException {

        //TODO perform edit to database ...

        return new ResponseEntity<>(new FileResponse(fileService.uploadFile(savePath, multipartFiles).toString()),
                    HttpStatus.OK);
    }

    @RequestMapping(value = "/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getFile(@PathVariable("filename") String filename, HttpServletResponse response) throws
            IOException {

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(this.fileService.getFile(savePath, filename), response.getOutputStream());
    }
}

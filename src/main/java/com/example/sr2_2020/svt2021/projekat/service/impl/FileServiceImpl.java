package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.dto.FileResponse;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.FileMapper;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.FileRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final TokenUtils tokenUtils;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileMapper fileMapper;

    @Override
    public ResponseEntity<FileResponse> uploadFile(String savePath, MultipartFile[] multipartFiles,
        Optional<Long> postIdForSave, HttpServletRequest request) throws IOException {

        User user = null;
        Post post = null;
        Long postId = null;

        if(postIdForSave.isPresent())
            postId = postIdForSave.get();

        if(postId == null) {

            String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

            user = userRepository.findByUsername(username).orElseThrow(() -> new
                    SpringRedditCloneException("User with username " + username + " not found"));

            if(multipartFiles.length > 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileResponse(null,
                        "Multiple images are not supported for user avatar"));

            if(!Objects.isNull(fileRepository.findByUser(user)))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileResponse(null,
                        "Avatar is already uploaded for provided user id, use PUT request to change it"));

        } else {

            post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postIdForSave.get()
                    .toString()));
        }


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

                fileRepository.save(fileMapper.mapDTOToFile(generatedFileName, post, user));

            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(fileNames.toString(),
                "File(s) are successfully uploaded to server"));
    }

    @Override
    public InputStream getFile(String path, String filename) throws FileNotFoundException {

        String fullPath = path + File.separator + filename;

        return new FileInputStream(fullPath);
    }
}

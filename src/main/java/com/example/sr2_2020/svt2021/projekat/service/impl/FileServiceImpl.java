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
import java.nio.file.Path;
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
        Long postId, HttpServletRequest request) throws IOException {

        User user = null;
        Post post = null;

        if(postId == null) {

            String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

            user = userRepository.findByUsername(username).orElseThrow(() -> new
                    SpringRedditCloneException("User with username " + username + " not found"));

            if(multipartFiles.length > 1) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileResponse(null,
                        "Multiple images are not supported for user avatar"));
            }

            if(!Objects.isNull(fileRepository.findByUser(user)))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileResponse(null,
                        "Avatar is already uploaded for provided user id, use PUT request to change it"));

        } else {

            post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        }


        List<String> fileNames = new ArrayList<>();

        File path = new File(savePath);

        boolean isDirectoryCreated = path.exists();

        if (!isDirectoryCreated) {

            isDirectoryCreated = path.mkdir();
        }

        if(isDirectoryCreated) {

            for(MultipartFile multipartFile : multipartFiles) {

                String generatedFilename = saveFile(savePath, multipartFile);

                fileNames.add(generatedFilename);

                fileRepository.save(fileMapper.mapDTOToFile(generatedFilename, post, user));

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

    @Override
    public ResponseEntity<FileResponse> replaceFile(String savePath, MultipartFile[] multipartFiles,
                                                    HttpServletRequest request) throws IOException {

        if(multipartFiles.length > 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileResponse(null,
                    "Multiple images are not supported for user avatar"));
        }

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        var fileForModifying = fileRepository.findByUser(user);

        removeFile(savePath, fileForModifying);

        String newFilename = saveFile(savePath, multipartFiles[0]);

        fileForModifying.setFileId(fileForModifying.getFileId());
        fileForModifying.setFilename(newFilename);

        fileRepository.save(fileForModifying);

        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(newFilename,
                "Avatar has been successfully changed"));
    }

    @Override
    public ResponseEntity<FileResponse> deleteFile(String savePath, String filename, HttpServletRequest request)
            throws IOException {

        var fileForDelete = fileRepository.findByFilename(filename).orElseThrow(() -> new
                FileNotFoundException("File " + filename + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        if(Objects.equals(fileForDelete.getUser(), null)) {

            Post post = postRepository.findById(fileForDelete.getPost().getPostId()).orElseThrow(() ->
                    new PostNotFoundException(fileForDelete.getPost().getPostId().toString()));

            if(Objects.equals(post.getUser().getUsername(), username)) {
                removeFile(savePath, fileForDelete);
                fileRepository.delete(fileForDelete);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new FileResponse(filename,
                        "You don't have right permissions to remove this image!"));
            }

        } else {
            if(Objects.equals(fileForDelete.getUser().getUsername(), username)) {
                removeFile(savePath, fileForDelete);
                fileRepository.delete(fileForDelete);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new FileResponse(filename,
                        "You don't have right permissions to remove this image!"));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(filename,
                "Image has been successfully deleted from server"));
    }

    private String saveFile(String savePath, MultipartFile multipartFile) throws IOException {

        String filename = multipartFile.getOriginalFilename();

        String randomID = UUID.randomUUID().toString();

        assert filename != null;
        String generatedFileName = randomID + "_" + filename.trim();

        String filePath = savePath + File.separator + generatedFileName;

        Files.copy(multipartFile.getInputStream(), Paths.get(filePath));

        return generatedFileName;
    }

    private void removeFile(String savePath, com.example.sr2_2020.svt2021.projekat.model.File file)
            throws IOException {

        String oldFilename = file.getFilename();

        String filePath = savePath + File.separator + oldFilename;

        Files.delete(Path.of(filePath));
    }

}

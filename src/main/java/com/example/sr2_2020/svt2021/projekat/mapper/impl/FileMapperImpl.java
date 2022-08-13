package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.mapper.FileMapper;
import com.example.sr2_2020.svt2021.projekat.model.File;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FileMapperImpl implements FileMapper {

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public File mapDTOToFile(String filename, Post post, User user) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new file ...");

        File.FileBuilder file = File.builder();

        file.filename(filename);
        file.post(post);
        file.user(user);

        logger.info("LOGGER: " + LocalDateTime.now() + " - File has been successfully mapped to object");

        return file.build();
    }
}

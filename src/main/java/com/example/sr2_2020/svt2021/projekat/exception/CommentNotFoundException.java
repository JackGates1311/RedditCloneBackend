package com.example.sr2_2020.svt2021.projekat.exception;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(String message) {

        super(message.toString());
    }
}

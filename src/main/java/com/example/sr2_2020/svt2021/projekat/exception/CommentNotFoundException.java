package com.example.sr2_2020.svt2021.projekat.exception;

public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(String message) {

        super(message.toString());
    }
}

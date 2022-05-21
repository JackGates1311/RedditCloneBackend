package com.example.sr2_2020.svt2021.projekat.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String message) {

        super(message.toString());
    }
}

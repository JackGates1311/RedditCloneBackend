package com.example.sr2_2020.svt2021.projekat.exception;

import java.io.IOException;

public class SpringRedditCloneException extends RuntimeException {

    public SpringRedditCloneException(String exMessage, IOException exception) {
        super(exMessage, exception);
    }

    public SpringRedditCloneException(String exMessage) {
        super(exMessage);
    }
}

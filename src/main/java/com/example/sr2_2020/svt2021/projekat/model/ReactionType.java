package com.example.sr2_2020.svt2021.projekat.model;

import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;

import java.util.Arrays;

public enum ReactionType {

    UPVOTE(1), DOWNVOTE(-1);

    private int direction;

    ReactionType(int direction) {}

    public static ReactionType find(Integer direction) {

        return Arrays.stream(ReactionType.values()).filter(value -> value.getDirection().equals(direction)).findAny().
                orElseThrow(() -> new SpringRedditCloneException("Reaction not found"));
    }

    public Integer getDirection() {

        return direction;
    }

}

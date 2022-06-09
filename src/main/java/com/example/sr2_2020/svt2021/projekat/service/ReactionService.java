package com.example.sr2_2020.svt2021.projekat.service;

import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Reaction;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

public interface ReactionService {

    @Transactional
    public void reaction(ReactionDTO reactionDTO, HttpServletRequest request);

    @Transactional
    public List<ReactionDTO> getReactionsByUsername(HttpServletRequest request);

}

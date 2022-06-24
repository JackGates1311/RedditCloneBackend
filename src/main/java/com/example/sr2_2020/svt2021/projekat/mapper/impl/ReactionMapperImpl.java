package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.ReactionMapper;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Reaction;
import com.example.sr2_2020.svt2021.projekat.model.Reaction.ReactionBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReactionMapperImpl implements ReactionMapper {

    @Override
    public Reaction mapDTOToReaction(ReactionDTO reactionDTO, Post post, User user) {

        if(reactionDTO == null)
            return null;

        ReactionBuilder reaction = Reaction.builder();

        reaction.reactionId(reactionDTO.getReactionId());
        reaction.reactionType(reactionDTO.getReactionType());
        reaction.timestamp(LocalDateTime.now());
        reaction.post(post);
        reaction.user(user);

        return reaction.build();
    }

    @Override
    public ReactionDTO mapToDTO(Reaction reaction) {

        if(reaction == null)
            return null;

        ReactionDTO reactionDTO = new ReactionDTO();

        reactionDTO.setReactionId(reaction.getReactionId());
        reactionDTO.setReactionType(reaction.getReactionType());
        reactionDTO.setPostId(reaction.getPost().getPostId());
        reactionDTO.setUserId(reaction.getUser().getUserId());

        reactionDTO.setUsername(reaction.getUser().getUsername());

        return reactionDTO;
    }

}

package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.mapper.ReactionMapper;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Reaction;
import com.example.sr2_2020.svt2021.projekat.model.Reaction.ReactionBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ReactionMapperImpl implements ReactionMapper {

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public Reaction mapDTOToReaction(ReactionDTO reactionDTO, Post post, User user, Comment comment) {

        if(reactionDTO == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - ReactionDTO body is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new reaction ...");

        ReactionBuilder reaction = Reaction.builder();

        reaction.reactionId(reactionDTO.getReactionId());
        reaction.reactionType(reactionDTO.getReactionType());
        reaction.timestamp(LocalDateTime.now());
        reaction.post(post);
        reaction.user(user);
        reaction.comment(comment);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Reaction has been successfully mapped to object");

        return reaction.build();
    }

    @Override
    public ReactionDTO mapToDTO(Reaction reaction) {

        if(reaction == null) {

            logger.info("LOGGER: " + LocalDateTime.now() + " - Reaction object is null ...");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new reaction response ...");

        ReactionDTO reactionDTO = new ReactionDTO();

        reactionDTO.setReactionId(reaction.getReactionId());
        reactionDTO.setReactionType(reaction.getReactionType());

        try {

            logger.info("LOGGER: " + LocalDateTime.now() + " - Trying to set post id ...");

            reactionDTO.setPostId(reaction.getPost().getPostId());

        } catch (Exception ignored) {

            logger.info("LOGGER: " + LocalDateTime.now() + " - Post id is null, trying to set comment id ...");

            reactionDTO.setCommentId(reaction.getComment().getCommentId());
        }

        reactionDTO.setUserId(reaction.getUser().getUserId());

        reactionDTO.setUsername(reaction.getUser().getUsername());

        logger.info("LOGGER: " + LocalDateTime.now() + " - New reaction has been successfully mapped to DTO");

        return reactionDTO;
    }

}

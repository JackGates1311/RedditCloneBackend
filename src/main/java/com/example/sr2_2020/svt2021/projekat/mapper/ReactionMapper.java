package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Reaction;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    public Reaction mapDTOToReaction (ReactionDTO reactionDTO, Post post, User user, Comment comment);

    public ReactionDTO mapToDTO(Reaction reaction);
}

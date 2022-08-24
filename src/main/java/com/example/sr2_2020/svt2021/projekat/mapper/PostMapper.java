package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "text", source = "postRequest.text")

    public abstract Post map(PostRequest postRequest, Community community, User user, List<Flair> flairs);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "community", source = "community")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "reactionCount", source = "reactionCount")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "flairs", source = "flairs")

    public abstract PostResponse mapToDTO(Post post, Integer commentCount, List<String> fileNames);

}

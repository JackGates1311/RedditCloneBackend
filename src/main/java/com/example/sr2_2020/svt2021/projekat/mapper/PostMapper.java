package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "text", source = "postRequest.text")

    public abstract Post map(PostRequest postRequest, Community community, User user);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "community", source = "community")
    @Mapping(target = "imagePath", source = "imagePath")
    @Mapping(target = "user", source = "user")

    @Mapping(target = "reactionCount", source = "reactionCount")

   // @Mapping(target = "community", source = "community")
    //@Mapping(target = "user", source = "user")

    public abstract PostResponse mapToDTO(Post post, Integer commentCount);

}

package com.example.sr2_2020.svt2021.projekat.mapper;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "text", source = "postRequest.text")

    Post map(PostRequest postRequest, String username);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "communityName", source = "communityName")
    @Mapping(target = "imagePath", source = "imagePath")
    @Mapping(target = "username", source = "username")

   // @Mapping(target = "community", source = "community")
    //@Mapping(target = "user", source = "user")

    PostResponse mapToDTO(Post post);

}

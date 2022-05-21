package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.mapper.PostMapper;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Post.PostBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post map(PostRequest postRequest, String username) {

        if(postRequest == null)
            return null;

        PostBuilder post = Post.builder();

        if(postRequest != null) {

            post.postId(postRequest.getPostId());
            post.communityName(postRequest.getCommunityName());
            post.creationDate(LocalDateTime.now());
            post.imagePath("");
            post.text(postRequest.getText());
            post.title(postRequest.getTitle());
            post.username(username);

        }

        //TODO Fix bug while registering new user with existing userName in DB

        return post.build();
    }

    @Override
    public PostResponse mapToDTO(Post post) {

        if(post == null)
            return null;

        PostResponse postResponse = new PostResponse();

        postResponse.setPostId(post.getPostId());
        postResponse.setCommunityName(post.getCommunityName());
        postResponse.setCreationDate(post.getCreationDate());
        postResponse.setImagePath(post.getImagePath());
        postResponse.setText(post.getText());
        postResponse.setTitle(post.getTitle());
        postResponse.setUsername(post.getUsername());

        return postResponse;

    }
}

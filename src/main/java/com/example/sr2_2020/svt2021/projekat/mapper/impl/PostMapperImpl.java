package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.mapper.PostMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Post.PostBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapperImpl extends PostMapper {

    @Override
    public Post map(PostRequest postRequest, Community community, User user) {

        if(postRequest == null)
            return null;

        PostBuilder post = Post.builder();

        post.postId(postRequest.getPostId());
        //post.communityId(postRequest.getCommunityId());
        post.creationDate(LocalDateTime.now());
        post.imagePath("");
        post.text(postRequest.getText());
        post.title(postRequest.getTitle());
        post.reactionCount(postRequest.getReactionCount());

        if(community != null) {

            post.community(community);

        }

        if(user != null) {

            post.user(user);
        }

        if(postRequest.getReactionCount() == null) {

            post.reactionCount(0);
        }

        return post.build();
    }

    @Override
    public PostResponse mapToDTO(Post post, Integer commentCount) {

        if(post == null)
            return null;

        PostResponse postResponse = new PostResponse();

        postResponse.setPostId(post.getPostId());
        postResponse.setCreationDate(post.getCreationDate().toString());
        postResponse.setImagePath(post.getImagePath());
        postResponse.setText(post.getText());
        postResponse.setTitle(post.getTitle());
        postResponse.setReactionCount(post.getReactionCount());

        postResponse.setCommunityName(postCommunityName(post));
        postResponse.setUsername(postUserName(post));

        postResponse.setCommentCount(commentCount);

        /* PostResponseBuilder postResponseDTO = PostResponse.builder();

        postResponseDTO.postId(post.getPostId());
        postResponseDTO.creationDate(post.getCreationDate());
        postResponseDTO.imagePath(post.getImagePath());
        postResponseDTO.text(post.getText());
        postResponseDTO.title(post.getTitle());
        postResponseDTO.username(post.getUsername());

        postResponseDTO.communityName(postCommunityName(post)); */

        return postResponse;

    }

    private String postUserName(Post post) {

        if(post == null)
            return null;

        User user = post.getUser();

        if(user == null)
            return null;

        String userName = user.getUsername();

        if(user.getUsername() == null)
            return null;

        return userName;
    }

    private String postCommunityName(Post post) {

        if(post == null)
            return null;

        Community community = post.getCommunity();

        if(community == null)
            return null;

        String communityName = community.getName();

        if(community.getName() == null)
            return null;

        return communityName;

    }
}
package com.example.sr2_2020.svt2021.projekat.mapper.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.mapper.PostMapper;
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.File;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Post.PostBuilder;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostMapperImpl extends PostMapper {

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public Post map(PostRequest postRequest, Community community, User user) {

        if(postRequest == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - PostRequest body is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new post ...");

        PostBuilder post = Post.builder();

        post.postId(postRequest.getPostId());
        post.creationDate(LocalDateTime.now());
        post.text(postRequest.getText());
        post.title(postRequest.getTitle());
        post.reactionCount(postRequest.getReactionCount());

        if(community != null) {

            post.community(community);

            logger.warn("LOGGER: " + LocalDateTime.now() + " - Community object is null ...");

        }

        if(user != null) {

            post.user(user);

            logger.warn("LOGGER: " + LocalDateTime.now() + " - User object is null ...");
        }

        if(postRequest.getReactionCount() == null) {

            logger.info("LOGGER: " + LocalDateTime.now() + " - Reaction count is null, setting to default value");

            post.reactionCount(0);
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - New post has been successfully mapped to object");

        return post.build();
    }

    @Override
    public PostResponse mapToDTO(Post post, Integer commentCount, List<String> fileNames) {

        if(post == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - Post object is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Building new post response ...");

        PostResponse postResponse = new PostResponse();

        postResponse.setPostId(post.getPostId());
        postResponse.setCreationDate(post.getCreationDate().toString());
        postResponse.setFileId(null);
        postResponse.setText(post.getText());
        postResponse.setTitle(post.getTitle());
        postResponse.setReactionCount(post.getReactionCount());
        postResponse.setCommunityName(postCommunityName(post));
        postResponse.setUsername(postUserName(post));
        postResponse.setCommentCount(commentCount);
        postResponse.setImages(fileNames);

        logger.info("LOGGER: " + LocalDateTime.now() + " - New post has been successfully mapped to DTO");

        return postResponse;

    }

    private String postUserName(Post post) {

        if(post == null) {

            logger.warn("LOGGER: " + LocalDateTime.now() + " - Post object is null");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting user data");

        User user = post.getUser();

        if(user == null) {

            logger.warn("LOGGER: " + LocalDateTime.now() + " - User object is null");

            return null;

        }

        String userName = user.getUsername();

        if(user.getUsername() == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - Failed to get username from user object");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Returning user username");

        return userName;
    }

    private String postCommunityName(Post post) {

        if(post == null) {

            logger.warn("LOGGER: " + LocalDateTime.now() + " - Post object is null");

            return null;
        }

        Community community = post.getCommunity();

        if(community == null) {

            logger.warn("LOGGER: " + LocalDateTime.now() + " - Community object is null");

            return null;
        }

        String communityName = community.getName();

        if(community.getName() == null) {

            logger.error("LOGGER: " + LocalDateTime.now() + " - Failed to get community name from community object");

            return null;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Returning community name");

        return communityName;

    }
}
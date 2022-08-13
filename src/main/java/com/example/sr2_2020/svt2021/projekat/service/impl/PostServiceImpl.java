package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommunityDTO;
import com.example.sr2_2020.svt2021.projekat.dto.PostRequest;
import com.example.sr2_2020.svt2021.projekat.dto.PostResponse;
import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.exception.CommunityNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommunityMapper;
import com.example.sr2_2020.svt2021.projekat.mapper.PostMapper;
import com.example.sr2_2020.svt2021.projekat.mapper.ReactionMapper;
import com.example.sr2_2020.svt2021.projekat.model.*;
import com.example.sr2_2020.svt2021.projekat.repository.*;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import com.example.sr2_2020.svt2021.projekat.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final CommunityRepository communityRepository;

    private final PostRepository postRepository;

    private final TokenUtils tokenUtils;

    private final CommunityMapper communityMapper;

    private final PostMapper postMapper;

    private final CommunityService communityService;

    private final ReactionRepository reactionRepository;

    private final UserRepository userRepository;

    private final ReactionMapper reactionMapper;

    private final CommentRepository commentRepository;

    private final FileRepository fileRepository;

    static final Logger logger = LogManager.getLogger(CommunityController.class);
    @Override
    public void save(PostRequest postRequest, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting data for saving post ...");

        Community community = communityRepository.findByNameAndIsSuspended(postRequest.getCommunityName(),
                        false).orElseThrow(() -> new CommunityNotFoundException("Community with name " +
                        postRequest.getCommunityName() + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        postRequest.setReactionCount(1);

        Post post = postRepository.save(postMapper.map(postRequest, community, user));

        ReactionDTO reactionDTO = new ReactionDTO();

        reactionDTO.setReactionType(ReactionType.UPVOTE);
        reactionDTO.setPostId(post.getPostId());

        logger.info("LOGGER: " + LocalDateTime.now() + " - saving post to database");

        reactionRepository.save(reactionMapper.mapDTOToReaction(reactionDTO, post, user, null));
    }

    @Override
    public List<PostResponse> getAllPosts() {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting all posts in database");

        return postRepository.findPostsByCommunity_IsSuspended(false).stream().map((Post post) ->
                postMapper.mapToDTO(post, commentRepository.countByPostAndIsDeleted(post, false),
                        fileRepository.findFilenameByPost(post))).collect(Collectors.toList());
    }

    @Override
    public PostResponse getPost(Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting post by id");

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        return postMapper.mapToDTO(post, commentRepository.countByPostAndIsDeleted(post, false),
                fileRepository.findFilenameByPost(post));
    }

    @Override
    public ResponseEntity<PostRequest> editPost(PostRequest postRequest, Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting post for edit ...");

        Community community = communityRepository.findByNameAndIsSuspended(postRequest.getCommunityName(),
                        false).orElseThrow(() -> new CommunityNotFoundException("Community with name " +
                        postRequest.getCommunityName() + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                UsernameNotFoundException("User with username " + username + " not found"));

        Post getPostForEdit = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        Post post = postMapper.map(postRequest, community, user);

        if(Objects.equals(getPost(id).getUsername(), username)) {

            post.setReactionCount(getPostForEdit.getReactionCount());

            post.setPostId(id);

            postRepository.save(post);

            logger.info("LOGGER: " + LocalDateTime.now() + " - Saving post data to database...");

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(postRequest);

        } else {

            logger.warn("LOGGER: " + LocalDateTime.now() + " - Trying to perform illegal operation");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }

    }

    @Override
    public ResponseEntity<?> deleteById(Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting post data...");

        PostResponse postResponse = getPost(id);

        if(Objects.equals(postResponse.getUsername(), tokenUtils.getUsernameFromToken(tokenUtils.getToken(request)))) {

            reactionRepository.deleteByPostPostId(id);

            postRepository.deleteById(id);

            return new ResponseEntity<>("Post has been deleted successfully", HttpStatus.ACCEPTED);

        } else {

            logger.warn("LOGGER: " + LocalDateTime.now() + " - Trying to perform illegal operation");

            return new ResponseEntity<>("You don't have permissions to delete this post", HttpStatus.FORBIDDEN);
        }


        //return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public List<PostResponse> getPostsByCommunityName(String communityName) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting post by name");

        CommunityDTO communityDTO = communityService.getCommunityByName(communityName);

        Community community = communityMapper.mapDTOToCommunity(communityDTO);

        return postRepository.findPostsByCommunity(community).stream().map((Post post) ->
                        postMapper.mapToDTO(post, commentRepository.countByPostAndIsDeleted(post,
                                false), fileRepository.findFilenameByPost(post))).collect(Collectors.toList());
    }
}
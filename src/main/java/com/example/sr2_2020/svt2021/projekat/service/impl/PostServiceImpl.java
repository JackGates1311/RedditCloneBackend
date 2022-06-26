package com.example.sr2_2020.svt2021.projekat.service.impl;

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
import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.ReactionType;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.CommunityRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.ReactionRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.AuthTokenFilter;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.CommunityService;
import com.example.sr2_2020.svt2021.projekat.service.PostService;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    AuthTokenFilter authTokenFilter;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    PostMapper postMapper;

    @Autowired
    CommunityService communityService;

    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReactionMapper reactionMapper;

    @Override
    public void save(PostRequest postRequest, HttpServletRequest request) {

        Community community = communityRepository.findByName(postRequest.getCommunityName()).
                orElseThrow(() -> new CommunityNotFoundException("Community with name " + postRequest.getCommunityName()
                        + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        postRequest.setReactionCount(1);

        Post post = postRepository.save(postMapper.map(postRequest, community, user));

        ReactionDTO reactionDTO = new ReactionDTO();

        reactionDTO.setReactionType(ReactionType.UPVOTE);
        reactionDTO.setPostId(post.getPostId());

        reactionRepository.save(reactionMapper.mapDTOToReaction(reactionDTO, post, user));
    }

    @Override
    public List<PostResponse> getAllPosts() {

        return postRepository.findAll().stream().map(postMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PostResponse getPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        return postMapper.mapToDTO(post);
    }

    @Override
    public ResponseEntity<PostRequest> editPost(PostRequest postRequest, Long id, HttpServletRequest request) {

        Community community = communityRepository.findByName(postRequest.getCommunityName()).
                orElseThrow(() -> new CommunityNotFoundException("Community with name " + postRequest.getCommunityName()
                        + " not found"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                UsernameNotFoundException("User with username " + username + " not found"));

        Post getPostForEdit = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));

        Post post = postMapper.map(postRequest, community, user);

        if(Objects.equals(getPost(id).getUsername(), username)) {

            post.setReactionCount(getPostForEdit.getReactionCount());

            post.setPostId(id);

            postRepository.save(post);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(postRequest);

        } else {

            return new ResponseEntity("You don't have permissions to edit this post", HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<?> deleteById(Long id, HttpServletRequest request) {

        PostResponse postResponse = getPost(id);

        if(Objects.equals(postResponse.getUsername(), tokenUtils.getUsernameFromToken(tokenUtils.getToken(request)))) {

            reactionRepository.deleteByPostPostId(id);

            postRepository.deleteById(id);

            return new ResponseEntity<>("Post has been deleted successfully", HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>("You don't have permissions to delete this post", HttpStatus.FORBIDDEN);
        }


        //return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public List<PostResponse> getPostsByCommunityName(String communityName) {

        CommunityDTO communityDTO = communityService.getCommunityByName(communityName);

        Community community = communityMapper.mapDTOToCommunity(communityDTO);

        return postRepository.findPostsByCommunity(community).stream().map(postMapper::mapToDTO).
                collect(Collectors.toList());
    }
}
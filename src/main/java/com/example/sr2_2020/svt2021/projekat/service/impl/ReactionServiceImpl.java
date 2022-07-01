package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.exception.CommentNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.ReactionMapper;
import com.example.sr2_2020.svt2021.projekat.model.*;
import com.example.sr2_2020.svt2021.projekat.repository.CommentRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.ReactionRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.ReactionService;
import com.example.sr2_2020.svt2021.projekat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.sr2_2020.svt2021.projekat.model.ReactionType.DOWNVOTE;
import static com.example.sr2_2020.svt2021.projekat.model.ReactionType.UPVOTE;

@Service
@AllArgsConstructor
@Slf4j
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ReactionMapper reactionMapper;

    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public void reaction(ReactionDTO reactionDTO, HttpServletRequest request) {

        //TODO refactor this method

        // Method written below is for saving post reaction!
        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting needed data for sending reaction");

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        logger.info("LOGGER: " + LocalDateTime.now() + " - Detecting type of reaction");

        if(reactionDTO.getCommentId() == null) {

            Post post = postRepository.findById(reactionDTO.getPostId()).orElseThrow(() ->
                    new PostNotFoundException("Post not found for specified post id"));

            Optional<Reaction> reactionByPostAndUser =
                    reactionRepository.findByPostAndUserOrderByReactionIdDesc(post, user);

            // da li reakcija na navedeni post postoji od strane istog korisnika?

            if(!reactionRepository.findByPostAndUserOrderByReactionIdDesc(post, user).isEmpty()) {

                reactionDTO.setReactionId(reactionByPostAndUser.get().getReactionId());

                // provera da li je stanje reackije null

                if(Objects.isNull(reactionByPostAndUser.get().getReactionType())) {

                    if(UPVOTE.equals(reactionDTO.getReactionType())) {

                        post.setReactionCount(post.getReactionCount() + 1);

                    } else {

                        post.setReactionCount(post.getReactionCount() - 1);
                    }

                } else {

                    // kliknuto je na istu reakciju kao i prethodni put

                    if(reactionByPostAndUser.get().getReactionType().equals(reactionDTO.getReactionType())) {

                        if(UPVOTE.equals(reactionDTO.getReactionType())) {

                            reactionDTO.setReactionType(null);
                            post.setReactionCount(post.getReactionCount() - 1);

                        } else {

                            reactionDTO.setReactionType(null);
                            post.setReactionCount(post.getReactionCount() + 1);
                        }

                    } else {

                        if(UPVOTE.equals(reactionDTO.getReactionType())) {

                            post.setReactionCount(post.getReactionCount() + 2);

                        } else {

                            post.setReactionCount(post.getReactionCount() - 2);
                        }
                    }

                }

            } else {

                if(UPVOTE.equals(reactionDTO.getReactionType())) {

                    post.setReactionCount(post.getReactionCount() + 1);

                } else {

                    post.setReactionCount(post.getReactionCount() - 1);
                }

            }

            logger.info("LOGGER: " + LocalDateTime.now() + " - Saving reaction to database");

            reactionRepository.save(reactionMapper.mapDTOToReaction(reactionDTO, post, user, null));

            postRepository.save(post);             // WARNING: You do not have this in your model

        } else {

            Comment comment = commentRepository.findById(reactionDTO.getCommentId()).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            Optional<Reaction> reactionByCommentAndUser =
                    reactionRepository.findByCommentAndUserOrderByReactionIdDesc(comment, user);

            if(!reactionRepository.findByCommentAndUserOrderByReactionIdDesc(comment, user).isEmpty()) {

                reactionDTO.setReactionId(reactionByCommentAndUser.get().getReactionId());

                if(Objects.isNull(reactionByCommentAndUser.get().getReactionType())) {

                    if(UPVOTE.equals(reactionDTO.getReactionType())) {

                        comment.setReactionCount(comment.getReactionCount() + 1);

                    } else {

                        comment.setReactionCount(comment.getReactionCount() - 1);
                    }

                } else {

                    if(reactionByCommentAndUser.get().getReactionType().equals(reactionDTO.getReactionType())) {

                        if(UPVOTE.equals(reactionDTO.getReactionType())) {

                            reactionDTO.setReactionType(null);
                            comment.setReactionCount(comment.getReactionCount() - 1);

                        } else {

                            reactionDTO.setReactionType(null);
                            comment.setReactionCount(comment.getReactionCount() + 1);
                        }

                    } else {

                        if(UPVOTE.equals(reactionDTO.getReactionType())) {

                            comment.setReactionCount(comment.getReactionCount() + 2);

                        } else {

                            comment.setReactionCount(comment.getReactionCount() - 2);
                        }

                    }

                }

            } else {

                if(UPVOTE.equals(reactionDTO.getReactionType())) {

                    comment.setReactionCount(comment.getReactionCount() + 1);

                } else {

                    comment.setReactionCount(comment.getReactionCount() - 1);

                }

            }

            reactionRepository.save(reactionMapper.mapDTOToReaction(reactionDTO, null , user, comment));

            commentRepository.save(comment);

        }



    }

    @Override
    public List<ReactionDTO> getReactionsByUsername(HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting reactions by username ...");

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        return reactionRepository.findByUser(user).stream().map(reactionMapper::mapToDTO).
                collect(Collectors.toList());
    }

}

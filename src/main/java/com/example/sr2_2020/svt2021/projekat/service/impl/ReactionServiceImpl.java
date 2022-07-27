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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.example.sr2_2020.svt2021.projekat.model.ReactionType.UPVOTE;

@Service
@AllArgsConstructor
@Slf4j
public class ReactionServiceImpl implements ReactionService {

    private final PostRepository postRepository;

    private final ReactionMapper reactionMapper;

    private final ReactionRepository reactionRepository;

    private final TokenUtils tokenUtils;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public void reaction(ReactionDTO reactionDTO, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting needed data for sending reaction");

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        logger.info("LOGGER: " + LocalDateTime.now() + " - Detecting type of reaction");

        saveReaction(reactionDTO, username);

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

    private void saveReaction(ReactionDTO reactionDTO, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        Post post = null;

        Comment comment = null;

        Optional<Reaction> reactionsByObjectAndUser;

        if(reactionDTO.getPostId() != null) {

            post = postRepository.findById(reactionDTO.getPostId()).orElseThrow(() ->
                    new PostNotFoundException("Post not found for specified post id"));

            reactionsByObjectAndUser = reactionRepository.findByPostAndUserOrderByReactionIdDesc(post, user);

        } else {

            comment = commentRepository.findById(reactionDTO.getCommentId()).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            reactionsByObjectAndUser = reactionRepository.findByCommentAndUserOrderByReactionIdDesc(comment, user);

        }

        if(reactionsByObjectAndUser.isPresent()) {

            reactionDTO.setReactionId(reactionsByObjectAndUser.get().getReactionId());

            if(Objects.isNull(reactionsByObjectAndUser.get().getReactionType())) {

                setReactionCount(reactionDTO, post, comment, 1);

            } else {

                if(reactionsByObjectAndUser.get().getReactionType().equals(reactionDTO.getReactionType())) {

                    setReactionCount(reactionDTO, post, comment, -1);

                    reactionDTO.setReactionType(null);

                } else {

                    setReactionCount(reactionDTO, post, comment, 2);

                }

            }

        } else {

            setReactionCount(reactionDTO, post, comment, 1);

        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Saving reaction to database");

        reactionRepository.save(reactionMapper.mapDTOToReaction(reactionDTO, post, user, comment));

        saveObjectCount(post, comment);

    }

    private void setReactionCount(ReactionDTO reactionDTO,Post post, Comment comment, Integer step) {

        Integer reactionCount;

        if(post != null)
            reactionCount = post.getReactionCount();
        else
            reactionCount = comment.getReactionCount();

        int newReactionCount;

        if(UPVOTE.equals(reactionDTO.getReactionType()))
            newReactionCount = reactionCount + step;
        else
            newReactionCount = reactionCount - step;

        updateReactionCount(post, comment, newReactionCount);

    }

    private void updateReactionCount(Post post, Comment comment, int newReactionCount) {

        if(post != null)
            post.setReactionCount(newReactionCount);
        else
            comment.setReactionCount(newReactionCount);
    }

    private void saveObjectCount(Post post, Comment comment) {

        if(post != null)
            postRepository.save(post);
        if(comment != null)
            commentRepository.save(comment);
    }

}

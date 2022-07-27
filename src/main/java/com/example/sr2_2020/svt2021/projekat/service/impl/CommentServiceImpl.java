package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.controller.CommunityController;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTORequest;
import com.example.sr2_2020.svt2021.projekat.dto.CommentDTOResponse;
import com.example.sr2_2020.svt2021.projekat.dto.ReactionDTO;
import com.example.sr2_2020.svt2021.projekat.exception.CommentNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.PostNotFoundException;
import com.example.sr2_2020.svt2021.projekat.exception.SpringRedditCloneException;
import com.example.sr2_2020.svt2021.projekat.mapper.CommentMapper;
import com.example.sr2_2020.svt2021.projekat.mapper.ReactionMapper;
import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.ReactionType;
import com.example.sr2_2020.svt2021.projekat.model.User;
import com.example.sr2_2020.svt2021.projekat.repository.CommentRepository;
import com.example.sr2_2020.svt2021.projekat.repository.PostRepository;
import com.example.sr2_2020.svt2021.projekat.repository.ReactionRepository;
import com.example.sr2_2020.svt2021.projekat.repository.UserRepository;
import com.example.sr2_2020.svt2021.projekat.security.TokenUtils;
import com.example.sr2_2020.svt2021.projekat.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final TokenUtils tokenUtils;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final ReactionRepository reactionRepository;

    private final  ReactionMapper reactionMapper;

    static final Logger logger = LogManager.getLogger(CommunityController.class);

    @Override
    public void save(CommentDTORequest commentDTORequest, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting data for saving comment ...");

        Post post = postRepository.findById(commentDTORequest.getPostId()).orElseThrow(() ->
                new PostNotFoundException("Post not found for specified post id"));

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new
                SpringRedditCloneException("User with username " + username + " not found"));

        Comment commentToVote;

        Comment reactionComment;

        logger.info("LOGGER: " + LocalDateTime.now() + " - Populating comment replies ...");

        if(commentDTORequest.getRepliedToCommentId() != null) {

            Comment comment = commentRepository.findById(commentDTORequest.getRepliedToCommentId()).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            Comment newComment = commentRepository.save(commentMapper.mapDTOToComment(commentDTORequest, post,
                    user));

            if(Objects.equals(comment.getReplies(), "")) {

                comment.setReplies(newComment.getCommentId().toString());

            } else {

                comment.setReplies(comment.getReplies() + "," + newComment.getCommentId().toString());
            }

            commentRepository.save(comment);

            reactionComment = newComment;

        } else {

            commentToVote = commentRepository.save(commentMapper.mapDTOToComment(commentDTORequest, post, user));

            reactionComment = commentToVote;
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - saving comment to database");

        logger.info("LOGGER: " + LocalDateTime.now() + " - performing automatic UPVOTE functionality");

        ReactionDTO reactionDTO = new ReactionDTO();

        reactionDTO.setReactionType(ReactionType.UPVOTE);

        reactionRepository.save(reactionMapper.mapDTOToReaction(reactionDTO, null, user, reactionComment));

        logger.info("LOGGER: " + LocalDateTime.now() + " - saving comment to database");

    }

    @Override
    public List<CommentDTOResponse> getPostComments(Long id, HttpServletRequest request) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(
                "Post not found for ID: " + id));

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting comments by post ID");

        List<CommentDTOResponse> commentDTOResponseList = new ArrayList<>();

        List<Long> childCommentsList = new ArrayList<>();

        for (Comment comment : commentRepository.findAllByPostAndIsDeleted(post, false)) {

            // get nested replies here

            List<CommentDTOResponse> replies;

            List<String> commentIdReplies = List.of(comment.getReplies().split(","));

            try {

                logger.info("LOGGER: " + LocalDateTime.now() + " - Checking for comment replies ...");

                List<CommentDTOResponse> list = new ArrayList<>();

                for (String commentIdReply : commentIdReplies) {
                    CommentDTOResponse commentDTOResponse = getComment(Long.valueOf(commentIdReply));
                    list.add(commentDTOResponse);
                    childCommentsList.add(Long.valueOf(commentIdReply));

                }

                replies = list;

            } catch (Exception ignored) {

                logger.info("LOGGER: " + LocalDateTime.now() + " - Comment replies not found, setting to null");

                replies = null;

            }

            CommentDTOResponse commentDTOResponse = commentMapper.mapCommentToDTO(comment, replies);

            commentDTOResponseList.add(commentDTOResponse);
        }

        logger.info("LOGGER: " + LocalDateTime.now() + " - Removing child comments from JSON structure");

        for (Long commentId : childCommentsList) {

            commentDTOResponseList.removeIf(response -> response.getCommentId().equals(commentId));

        }

        return commentDTOResponseList;
    }

    @Override
    public ResponseEntity<String> editComment(CommentDTORequest commentDTORequest, Long id,
                                              HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting comment data ...");

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        Comment commentForEdit = commentRepository.findById(id).orElseThrow(() -> new
                CommentNotFoundException("Comment not found with specified commentId"));

        if(Objects.equals(commentForEdit.getUser().getUsername(), username)) {

            commentForEdit.setCommentId(id);
            commentForEdit.setText(commentDTORequest.getText());

            commentRepository.save(commentForEdit);

            logger.info("LOGGER: " + LocalDateTime.now() + " - Comments has been successfully saved");

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Comment has been successfully edited");

        }

        logger.warn("LOGGER: " + LocalDateTime.now() + " - Trying to perform illegal operation");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permissions to edit this comment");

    }

    List<Long> commentIdsToDelete;

    @Override
    public ResponseEntity<String> deleteComment(Long id, HttpServletRequest request) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Deleting parent comments and its child elements");

        commentIdsToDelete.clear();

        return getCommentsToDelete(id, request);
    }

    @Override
    public CommentDTOResponse getComment(Long id) {

        logger.info("LOGGER: " + LocalDateTime.now() + " - Getting comment by comment ID");

        Comment comment = commentRepository.findByCommentIdAndIsDeleted(id, false);

        List<CommentDTOResponse> replies;

        List<String> commentIdReplies = List.of(comment.getReplies().split(","));

        try {

            // here filter replies that not exists!

            logger.info("LOGGER: " + LocalDateTime.now() + " - Trying to set comment replies");

            replies = commentIdReplies.stream().map(commentIdReply ->
                    getComment(Long.valueOf(commentIdReply))).collect(Collectors.toList());

        } catch (Exception ignored) {

            logger.info("LOGGER: " + LocalDateTime.now() + " - Comment replies not found, setting to null");

            replies = null;
        }

        return commentMapper.mapCommentToDTO(comment, replies);

    }

    private ResponseEntity<String> getCommentsToDelete(Long id, HttpServletRequest request) {

        boolean isFinished = false;
        boolean isParentComment = true;

        String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));

        while(!isFinished) {

            Comment parentComment = commentRepository.findById(id).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            if(!Objects.equals(parentComment.getUser().getUsername(), username) && isParentComment) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        "You don't have permissions to delete this comment");
            }

            isParentComment = false;

            commentIdsToDelete.add(parentComment.getCommentId());

            List<String> parentCommentIdReplies = List.of(parentComment.getReplies().split(","));

            if(parentCommentIdReplies.get(0).equals("")) {

                logger.info("LOGGER: " + LocalDateTime.now() + " - Identification finished");

                isFinished = true;

            } else {

                logger.info("LOGGER: " + LocalDateTime.now() + " - Finding comment ID for delete ...");

                Comment childComment;

                int i = 0;

                while (i < parentCommentIdReplies.size()) {

                    childComment = commentRepository.findById(Long.parseLong(
                            parentCommentIdReplies.get(i))).orElseThrow(() ->
                            new CommentNotFoundException("Comment not found with specified commentId"));

                    commentIdsToDelete.add(Long.valueOf(parentCommentIdReplies.get(i)));

                    i++;

                    id = childComment.getCommentId();

                    getCommentsToDelete(id, request);

                }


            }

        }

        commentIdsToDelete = commentIdsToDelete.stream().distinct().collect(Collectors.toList());

        deleteComments(commentIdsToDelete);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Updating comment replies ...");

        updateCommentReplies(commentIdsToDelete);

        logger.info("LOGGER: " + LocalDateTime.now() + " - Comments are successfully removed ...");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Comment has been successfully deleted");

    }

    private void deleteComments(List<Long> commentIdsToDelete) {

        for (Long commentId : commentIdsToDelete) {

            Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                    new CommentNotFoundException("Comment not found with specified commentId"));

            comment.setIsDeleted(true);

            commentRepository.save(comment);
        }
    }


    private void updateCommentReplies(List<Long> commentIdsToDelete) {

        //TODO fix comment glitches (little bugs*) and method

        // update replies here

        // get all comments

        List<Comment> comments = commentRepository.findAll();

        for(Comment comment : comments) {

            List<String> commentReplies = List.of(comment.getReplies().split(","));

            StringBuilder updatedCommentRepliesBuilder = new StringBuilder();

            for(String commentReply: commentReplies) {

                for(Long commentIdToDelete: commentIdsToDelete) {

                    if(!commentReply.equals(commentIdToDelete.toString())) {

                        updatedCommentRepliesBuilder.append(commentReply).append(",");
                    }
                }
            }

            String updatedCommentReplies = updatedCommentRepliesBuilder.toString();

            if(!updatedCommentReplies.equals(""))
                updatedCommentReplies = updatedCommentReplies.substring(0, updatedCommentReplies.length() - 1);

            comment.setReplies(updatedCommentReplies);

            commentRepository.save(comment);
        }

        //
    }

}

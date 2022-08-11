package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostAndIsDeleted (Post post, Boolean isDeleted);

    Integer countByPostAndIsDeleted (Post post, Boolean isDeleted);

    Comment findByCommentIdAndIsDeleted (Long commentId, Boolean isDeleted);

    @Query("SELECT SUM(reactionCount) FROM Comment WHERE user = ?1")
    Integer sumReactionCountByUser(User user);

}

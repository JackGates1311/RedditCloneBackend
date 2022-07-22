package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Reaction;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByPostAndUserOrderByReactionIdDesc(Post post, User user);

    Optional<Reaction> findByCommentAndUserOrderByReactionIdDesc(Comment comment, User user);

    List<Reaction> findByUser(User user);

    @Transactional
    void deleteByPostPostId(Long postId);

}

package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.Community;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsByCommunity(Community community);

    @Query("SELECT SUM(reactionCount) FROM Post WHERE user = ?1")
    Integer sumReactionCountByUser(User user);

    List<Post> findPostsByCommunity_IsSuspended(Boolean isSuspended);

}

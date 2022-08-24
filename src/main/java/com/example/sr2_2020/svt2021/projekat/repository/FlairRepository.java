package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.Flair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FlairRepository extends JpaRepository<Flair, Long> {

    Optional<Flair> findByName(String flairName);

    @Query(value = "SELECT flair.flair_id, flair.name FROM flair " +
            "LEFT OUTER JOIN community_flair ON flair.flair_id = community_flair.flair_id " +
            "WHERE flair.name = ?1 AND community_flair.community_id = ?2", nativeQuery = true)
    Optional<Flair> findByNameAndCommunityId(String flairName, Long communityId);

    @Query(value = "SELECT flair.flair_id, flair.name FROM flair\n" +
            "    LEFT OUTER JOIN community_flair cf ON flair.flair_id = cf.flair_id\n" +
            "    LEFT OUTER JOIN post_flair pf ON flair.flair_id = pf.flair_id\n" +
            "    WHERE pf.post_id IS NULL AND cf.community_id IS NULL AND flair.name = ?1", nativeQuery = true)
    Optional<Flair> findEditableByName(String flairName);;

    @Query(value = "SELECT flair.name FROM post " +
            "LEFT OUTER JOIN post_flair ON post_flair.post_id = post.post_id " +
            "LEFT OUTER JOIN community ON community.community_id = post.community_id " +
            "LEFT OUTER JOIN flair ON post_flair.flair_id = flair.flair_id " +
            "WHERE community.community_id = ?1", nativeQuery = true)
    List<String> findFlairsUsedInPostsByCommunityId(Long communityId);

    void deleteByName(String name);
}

package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    Optional<Community> findByName (String communityName);

}

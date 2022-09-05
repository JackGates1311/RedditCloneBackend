package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.Report;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByPostAndUser(Post post, User user);

    Optional<Report> findByCommentAndUser(Comment comment, User user);

}

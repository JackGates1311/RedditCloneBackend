package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.File;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    File findByUser (User user);

    List<File> findAllByUserIsNull();

    List<File> findByPost(Post post);
    @Query("SELECT filename FROM File WHERE post = ?1")
    List<String> findFilenameByPost(Post post);
}

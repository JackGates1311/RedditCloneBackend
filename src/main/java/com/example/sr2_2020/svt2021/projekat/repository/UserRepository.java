package com.example.sr2_2020.svt2021.projekat.repository;

import com.example.sr2_2020.svt2021.projekat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername (String username);

}

package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Banned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannedId;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "communityId", referencedColumnName = "communityId")
    private Community community;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}

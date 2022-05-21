package com.example.sr2_2020.svt2021.projekat.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotBlank(message = "Community name cannot be empty")
    private String communityName;

    private LocalDateTime creationDate;

    private String imagePath;

    @NotBlank(message = "Post text cannot be empty")
    private String text;

    @NotBlank(message = "Post Title cannot be empty")
    private String title;

    @NotBlank(message = "Community name cannot be empty")
    private String username;

    /*@ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Community community;*/







}

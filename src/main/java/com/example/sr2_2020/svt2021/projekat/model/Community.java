package com.example.sr2_2020.svt2021.projekat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Community {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long communityId;

    private LocalDateTime creationDate;

    @NotBlank(message = "Community description is required")
    private String description;

    private Boolean isSuspended = false;

    @NotBlank(message = "Community name is required")
    @Column(unique=true)
    private String name;

    @OneToMany(fetch = LAZY)
    private List<Post> posts;

    private String suspendedReason;

}

package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class File {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = Post.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Post post;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = User.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private User user;

    @Lob
    @Column(length = 500)
    @NotBlank(message = "Filename cannot be empty")
    private String filename;

}

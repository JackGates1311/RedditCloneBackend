package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private ReportReason reportReason;

    @Lob
    @Column(length = 65535)
    private String reportDescription;

    private LocalDateTime timestamp;

    private Boolean accepted;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = User.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private User user;

    @ManyToOne(cascade = CascadeType.REFRESH ,targetEntity = Post.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Post post;

    @ManyToOne(cascade = CascadeType.REFRESH ,targetEntity = Comment.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Comment comment;

}

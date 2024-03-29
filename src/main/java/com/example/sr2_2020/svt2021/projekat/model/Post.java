package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private LocalDateTime creationDate;

    @Lob
    @Column(length = 65535)
    @NotBlank(message = "Post text cannot be empty")
    private String text;

    @NotBlank(message = "Post title cannot be empty")
    private String title;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = User.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = Community.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "communityId", referencedColumnName = "communityId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Community community;

    private Integer reactionCount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="post_flair", joinColumns = { @JoinColumn(name = "post_id")},
            inverseJoinColumns = { @JoinColumn (name = "flair_id")})
    private Set<Flair> flair = new HashSet<>();

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        Post post = (Post) o;

        return postId != null && Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}
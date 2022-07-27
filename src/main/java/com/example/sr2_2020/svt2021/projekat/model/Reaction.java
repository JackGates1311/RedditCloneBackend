package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    private ReactionType reactionType;

    private LocalDateTime timestamp;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = Post.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Post post;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = Comment.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Comment comment;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = User.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        Reaction reaction = (Reaction) o;

        return reactionId != null && Objects.equals(reactionId, reaction.reactionId);

    }

    @Override
    public int hashCode() {

        return getClass().hashCode();

    }
}

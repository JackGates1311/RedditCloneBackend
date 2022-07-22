package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import static javax.persistence.FetchType.EAGER;

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

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
    private Comment comment;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
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

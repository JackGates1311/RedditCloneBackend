package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Lob
    @Column(length = 65535)
    @NotEmpty
    private String text;

    private LocalDateTime timestamp;

    private Boolean isDeleted;

    private String replies;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    private Integer reactionCount;

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        Comment comment = (Comment) o;

        return commentId != null && Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}

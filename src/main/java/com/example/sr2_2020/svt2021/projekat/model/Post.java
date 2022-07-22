package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import static javax.persistence.FetchType.EAGER;

//TODO try to use Lazy loading where is possible...

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

    private String imagePath;

    @Lob
    @Column(length = 65535)
    @NotBlank(message = "Post text cannot be empty")
    private String text;

    @NotBlank(message = "Post title cannot be empty")
    private String title;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "communityId", referencedColumnName = "communityId")
    private Community community;

    private Integer reactionCount;

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
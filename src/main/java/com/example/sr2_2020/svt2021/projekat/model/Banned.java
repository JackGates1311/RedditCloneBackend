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

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        Banned banned = (Banned) o;

        return bannedId != null && Objects.equals(bannedId, banned.bannedId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}

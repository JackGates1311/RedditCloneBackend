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
public class Banned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannedId;

    private LocalDateTime timestamp;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = Community.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "communityId", referencedColumnName = "communityId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private Community community;

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

        Banned banned = (Banned) o;

        return bannedId != null && Objects.equals(bannedId, banned.bannedId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}

package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")

public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;

    @OneToOne(fetch = LAZY)
    @ToString.Exclude
    private User user;
    private LocalDateTime expirationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        VerificationToken that = (VerificationToken) o;

        return tokenId != null && Objects.equals(tokenId, that.tokenId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}

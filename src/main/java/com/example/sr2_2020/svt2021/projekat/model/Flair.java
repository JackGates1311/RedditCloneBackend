package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Flair {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long flairId;

    @Column(length = 50, unique = true)
    @NotBlank(message = "Flair name cannot be empty")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "flair") //TODO make it lazy
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "flair")
    private Set<Community> communities = new HashSet<>();

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        Flair flair = (Flair) o;

        return flairId != null && Objects.equals(flairId, flair.flairId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}

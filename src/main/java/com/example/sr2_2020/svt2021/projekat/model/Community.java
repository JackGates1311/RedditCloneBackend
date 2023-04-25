package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Community {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long communityId;

    private LocalDateTime creationDate;

    @Lob
    @Column(length = 65535)
    @NotBlank(message = "Community description is required")
    private String description;

    private Boolean isSuspended = false;

    @NotBlank(message = "Community name is required")
    @Column(unique=true)
    private String name;

    @OneToMany(fetch = LAZY)
    @ToString.Exclude
    private List<Post> posts;

    private String suspendedReason;

    //https://www.youtube.com/watch?v=-q9rp2pzvGU

    @ManyToMany(fetch = FetchType.EAGER) // TODO try to use lazy loading
    @JoinTable(name="community_flair", joinColumns = { @JoinColumn(name = "community_id")},
            inverseJoinColumns = { @JoinColumn (name = "flair_id")})
    private Set<Flair> flair = new HashSet<>();

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        Community community = (Community) o;

        return communityId != null && Objects.equals(communityId, community.communityId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}

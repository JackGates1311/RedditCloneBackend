package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

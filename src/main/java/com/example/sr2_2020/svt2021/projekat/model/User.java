package com.example.sr2_2020.svt2021.projekat.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(cascade = CascadeType.MERGE ,targetEntity = File.class, fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "fileId", referencedColumnName = "fileId")
    @LazyToOne(LazyToOneOption.PROXY)
    @ToString.Exclude
    private File file;

    @NotBlank(message = "Username is required")
    @Column(unique=true)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @Email
    @NotEmpty(message = "Email is required")
    private String email;

    private LocalDateTime registrationDate;

    @Lob
    @Column(length = 65535)
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Display name is required")
    private String displayName;

    private Boolean isAdministrator;

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;

        User user = (User) o;

        return userId != null && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}

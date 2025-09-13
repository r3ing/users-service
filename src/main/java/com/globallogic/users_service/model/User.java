package com.globallogic.users_service.model;


import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user entity in the system. This class is mapped to the "users" table in the database
 * using JPA annotations. It includes relevant user information such as name, email, password,
 * creation timestamp, last login, token, and active status. Additionally, it maintains a
 * relationship with the {@link Phone} entity, representing a user's associated phone numbers.
 */
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Instant created;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(nullable = false)
    private String token;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Phone> phones = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        created = now;
        lastLogin = now;
        isActive = true;
    }

}

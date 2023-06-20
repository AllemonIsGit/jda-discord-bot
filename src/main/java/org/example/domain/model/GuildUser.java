package org.example.domain.model;

import jakarta.persistence.*;

@Entity
public class GuildUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;
    @Column(nullable = false)
    private String userId;
    private String username;
    @Column(nullable = false)
    private Integer points;
}

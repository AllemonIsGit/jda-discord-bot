package org.example.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class GuildUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;
    @Column(nullable = false)
    private String snowflakeId;
    private String discordTag;
    @Column(nullable = false)
    private Integer points;


    // This does not update the entity in the database
    public void addPoints(Integer amount) {
        this.points += amount;
    }

    // This does not update the entity in the database
    public void subtractPoints(Integer amount) {
        this.points -= amount;
    }
}

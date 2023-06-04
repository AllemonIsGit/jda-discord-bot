package org.example.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "insults")
@Data
@NoArgsConstructor
public class Insult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;
    @Column(name = "insult")
    private String insult;

    public Insult(String insult) {
        this.insult = insult;
    }
}

package org.example.model;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Table(name = "insults")
@Data
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

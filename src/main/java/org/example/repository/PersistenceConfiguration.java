package org.example.repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistenceConfiguration {

    private static PersistenceConfiguration INSTANCE;

    @Getter
    @Setter
    private String hibernateConfigurationFile;

    public static PersistenceConfiguration getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PersistenceConfiguration();
        }

        return INSTANCE;
    }
}

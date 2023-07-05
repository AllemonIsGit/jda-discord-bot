package org.example.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleMessage {
    private String messageId;
    private String authorSnowflakeId;
}

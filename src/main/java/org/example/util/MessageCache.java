package org.example.util;

import org.example.domain.exception.CachedMessageException;

import java.util.ArrayList;
import java.util.List;

public class MessageCache {
    private static MessageCache INSTANCE;
    private final List<SimpleMessage> messages;
    private final Integer cacheLimit = 1000;

    private MessageCache() {
        this.messages = new ArrayList<>();
    }

    public static MessageCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageCache();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public void add(SimpleMessage message) {
        if (messages.size() > cacheLimit) {
            messages.remove(0);
        }
        messages.add(message);
    }

    public boolean existsByMessageId(String messageId) {
        return messages.stream()
                .anyMatch(message -> message.getMessageId().equals(messageId));
    }

    public String getAuthorIdByMessageId(String messageId) {
        return messages.stream()
                .filter(e -> e.getMessageId().equals(messageId))
                .map(SimpleMessage::getAuthorSnowflakeId)
                .findFirst()
                .orElseThrow(() -> new CachedMessageException("Can't find cached author."));
    }
}

package org.example.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EventService {
    private static EventService INSTANCE;
    private final String prefix = "!";

    public static EventService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventService();
            return INSTANCE;
        }
        return INSTANCE;
    }

    private EventService() {
    }

    private List<String> splitMessageOnSpace(String message) {
        String[] splitMessage = message.split(" ");
        return new ArrayList<>(Arrays.asList(splitMessage));
    }

    private String removePrefix(String message) {
        return message.substring(1);
    }

    public Optional<List<String>> handleMessageEvent(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (!message.startsWith(prefix) || event.getAuthor().isBot()) {
            return Optional.empty();
        }

        String noPrefixMessage = removePrefix(message);
        return Optional.of(splitMessageOnSpace(noPrefixMessage));
    }
}

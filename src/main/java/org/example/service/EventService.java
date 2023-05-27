package org.example.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventService {
    private final String prefix = "!";

    public String getMessageFromEvent(MessageReceivedEvent event) {
        return event.getMessage().getContentRaw();
    }

    public Boolean checkForPrefix(String message) {
        return message.startsWith(prefix);
    }

    public List<String> splitMessageOnSpace(String message) {
        String[] splitMessage = message.split(" ");
        return new ArrayList<>(Arrays.asList(splitMessage));
    }

    public List<String> removePrefix(List<String> message) {
        String firstString = message.get(0);
        firstString = firstString.substring(1);
        message.set(0, firstString);
        return message;
    }
}

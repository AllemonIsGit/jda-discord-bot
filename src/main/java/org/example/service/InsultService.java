package org.example.service;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InsultService {
    private final List<String> insults = new ArrayList<>();

    public void insult(MessageReceivedEvent event) {
        User author = event.getAuthor();
        event.getChannel().sendMessage(author.getAsMention() + " " + getRandomInsult()).queue();
    }

    public void register(String insult) {
        insults.add(insult);
    }

    private String getRandomInsult() {
        Random random = new Random();

        if (insults.size() == 0) {
            return "I don't have any insults on my list.";
        }

        return insults.get(random.nextInt(insults.size()));
    }
}

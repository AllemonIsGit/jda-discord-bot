package org.example.service;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InsultService {
    private final static List<String> insults = new ArrayList<>();

    public static void Insult(MessageReceivedEvent event) {
        User author = event.getAuthor();
        event.getChannel().sendMessage(author.getAsMention() + " " + getRandomInsult()).queue();
    }
    public static void Register(String insult) {
        insults.add(insult);
    }
    private static String getRandomInsult() {
        Random random = new Random();

        return insults.get(random.nextInt(insults.size()));
    }
}

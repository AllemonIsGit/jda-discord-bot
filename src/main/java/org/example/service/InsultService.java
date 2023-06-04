package org.example.service;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.domain.exception.InsultNotFoundException;
import org.example.domain.model.Insult;
import org.example.repository.InsultRepository;

import java.util.List;
import java.util.Random;

public class InsultService {
    private static InsultService INSTANCE;
    private final List<Insult> insults;
    private final InsultRepository insultRepository;

    private InsultService() {
        this.insultRepository = InsultRepository.getInstance();

        this.insults = insultRepository.findAll();
    }

    public void insult(MessageReceivedEvent event) {
        User author = event.getAuthor();
        event.getChannel().sendMessage(author.getAsMention() + " " + getRandomInsult().getInsult()).queue();
    }

    public void register(String insult) {
        Insult insultObject = mapToInsult(insult);
        insults.add(insultObject);
        insultRepository.save(insultObject);

    }

    private Insult getRandomInsult() {
        Random random = new Random();

        if (insults.size() == 0) {
            throw new InsultNotFoundException("No insults found.");
        }

        return insults.get(random.nextInt(insults.size()));
    }

    private Insult mapToInsult(String text) {
        return new Insult(text);
    }

    public static InsultService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InsultService();
            return INSTANCE;
        }
        return INSTANCE;
    }
}

package org.example.gamble.interaction.gamble;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.gamble.WeightedRandomizer;
import org.example.gamble.interaction.base.Interaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GambleInteraction extends Interaction<MessageReceivedEvent, Void> {

    private final WeightedRandomizer randomizer = new WeightedRandomizer();

    private final Interaction<MessageReceivedEvent, List<User>> collectGamblersInteraction = new CollectGamblersInteraction();

    @Override
    public CompletableFuture<Void> apply(MessageReceivedEvent input) {
        final TextChannel targetChannel = input.getChannel().asTextChannel();
        return collectGamblersInteraction.apply(input)
                .thenApply(this::selectWinner)
                .thenCompose(winner -> announceWinner(winner, targetChannel));
    }

    private User selectWinner(List<User> users) {
        return randomizer.pick(users, $ -> 1d);
    }

    private CompletableFuture<Void> announceWinner(User winner, TextChannel textChannel) {
        return textChannel.sendMessage(String.format("User %s won the gamble!", winner.getAsMention()))
                .submit()
                .thenApply($ -> null);
    }
}

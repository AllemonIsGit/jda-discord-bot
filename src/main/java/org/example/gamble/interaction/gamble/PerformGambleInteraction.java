package org.example.gamble.interaction.gamble;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.gamble.Gamble;
import org.example.gamble.WeightedRandomizer;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.utils.Futures;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class PerformGambleInteraction extends Interaction<MessageReceivedEvent, Void> {

    private static final String POOP = ":poop:";

    private static final String MONEY_MOUTH = ":money_mouth:";

    private final WeightedRandomizer randomizer = new WeightedRandomizer();

    private final Interaction<MessageReceivedEvent, Gamble> prepareGambleInteraction;

    public PerformGambleInteraction() {
        this(new PrepareGambleInteraction());
    }

    @Override
    public CompletableFuture<Void> apply(MessageReceivedEvent input) {
        final TextChannel targetChannel = input.getChannel().asTextChannel();

        return prepareGambleInteraction.apply(input)
                .thenCompose(gamble -> performGamble(targetChannel, gamble));
    }

    private CompletableFuture<Void> performGamble(TextChannel targetChannel, Gamble gamble) {
        if (gamble.getParticipants().isEmpty()) {
            return noParticipantsCancellation(targetChannel);
        }

        return doFancyStuff(targetChannel)
                .thenApply($ -> selectWinner(gamble))
                .thenCompose(winner -> announceWinner(winner, targetChannel));
    }

    private CompletableFuture<Void> noParticipantsCancellation(TextChannel textChannel) {
        return textChannel.sendMessage("Gambling cancelled, no participants registered. " + POOP).submit()
                .thenCompose(Futures.asVoid());
    }

    private CompletableFuture<Void> doFancyStuff(TextChannel textChannel) {
        return textChannel.sendMessage("Gambling").submit()
                .thenCompose(Futures.iterating(this::dottedWait, 5))
                .thenCompose(this::displayFace)
                .thenCompose(Futures.asVoid());
    }

    private CompletableFuture<Message> dottedWait(Message message) {
        final String messageContent = message.getContentRaw();
        return Futures.waitMillis(1000)
                .thenCompose($ -> message.editMessage(messageContent + ".").submit());
    }

    private CompletableFuture<Message> displayFace(Message message) {
        final String messageContent = message.getContentRaw();
        return Futures.waitMillis(2000)
                .thenCompose($ -> message.editMessage(messageContent + " " + MONEY_MOUTH).submit());
    }

    private GambleWinner selectWinner(Gamble gamble) {
        final User winner = randomizer.pick(gamble.getParticipants(), gamble::getBetForUser);
        return new GambleWinner(winner, gamble.getTotalBet());
    }

    private CompletableFuture<Void> announceWinner(GambleWinner winner, TextChannel textChannel) {
        final String message = String.format("User %s won %d points!", winner.user().getAsMention(), winner.prize());
        return textChannel.sendMessage(message)
                .submit()
                .thenCompose(Futures.asVoid());
    }

    private record GambleWinner(User user, long prize) {
    }
}

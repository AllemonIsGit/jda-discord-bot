package org.example.gamble.interaction.gamble;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.gamble.Gamble;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.interaction.base.TimeRemainingInteraction;
import org.example.gamble.utils.Futures;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PrepareGambleInteraction extends Interaction<MessageReceivedEvent, Gamble> {

    private static final Predicate<String> GAMBLE_ENTRY_MESSAGE_PREDICATE = Pattern.compile("^!bet -?\\d+$")
            .asMatchPredicate();

    private static final List<Duration> DEFAULT_DURATIONS = List.of(
            Duration.ofSeconds(30),
            Duration.ofSeconds(20),
            Duration.ofSeconds(10));

    private final Interaction<TextChannel, Void> timeRemainingNotificationInteraction;

    public PrepareGambleInteraction() {
        this(new TimeRemainingInteraction(DEFAULT_DURATIONS));
    }

    @Override
    public CompletableFuture<Gamble> apply(MessageReceivedEvent input) {
        final TextChannel targetChannel = input.getChannel().asTextChannel();
        final User initiator = input.getAuthor();
        final JDA jda = input.getJDA();
        final GamblerEntryMessageListener entryMessageListener = new GamblerEntryMessageListener(targetChannel);

        jda.addEventListener(entryMessageListener);
        return announceGamblePreparation(targetChannel, initiator)
                .thenCompose($ -> timeRemainingNotificationInteraction.apply(targetChannel))
                .thenAccept($ -> jda.removeEventListener(entryMessageListener))
                .thenCompose($ -> notifyCollectionEnded(targetChannel))
                .thenApply($ -> entryMessageListener.getGamble())
                .thenCompose(Futures.peeking(gamble -> announceTotalBet(gamble, targetChannel)));
    }

    private CompletableFuture<Void> announceGamblePreparation(TextChannel targetChannel, User initiator) {
        final String message = String.format("User %s initiated gamble! Waiting 1 minute for participants.", initiator.getAsMention());
        return targetChannel.sendMessage(message).submit()
                .thenCompose(Futures.asVoid());
    }

    private CompletableFuture<Void> notifyCollectionEnded(TextChannel targetChannel) {
        return targetChannel.sendMessage("Gamble registration closed!").submit()
                .thenCompose(Futures.asVoid());
    }

    private CompletableFuture<Void> announceTotalBet(Gamble gamble, TextChannel targetChannel) {
        return targetChannel.sendMessage("Total bet is: " + gamble.getTotalBet()).submit()
                .thenCompose(Futures.asVoid());
    }

    @RequiredArgsConstructor
    private static class GamblerEntryMessageListener extends ListenerAdapter {

        private final Channel targetChannel;

        @Getter
        private final Gamble gamble = new Gamble();

        @Override
        public void onMessageReceived(@NotNull MessageReceivedEvent event) {
            if (!isInTargetChannel(event.getChannel())) {
                return;
            }

            final Message message = event.getMessage();
            if (!GAMBLE_ENTRY_MESSAGE_PREDICATE.test(message.getContentRaw())) {
                return;
            }

            final Optional<Integer> parsedBet = parseBet(message);
            if (parsedBet.isEmpty()) {
                deleteMessage(message);
                return;
            }

            final int bet = parsedBet.get();
            if (gamble.addGambler(message.getAuthor(), bet)) {
                acknowledgeEntry(message, bet);
            } else {
                deleteMessage(message);
            }
        }

        private boolean isInTargetChannel(Channel messageChannel) {
            return targetChannel.getId().equals(messageChannel.getId());
        }

        private Optional<Integer> parseBet(Message message) {
            final String[] gamblerStrings = message.getContentRaw().split(" ");

            if (gamblerStrings.length < 2) {
                return Optional.empty();
            }

            try {
                final int bet = Integer.parseInt(gamblerStrings[1]);
                return Optional.of(bet);
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        private void acknowledgeEntry(Message message, int bet) {
            message.getChannel()
                    .sendMessage(String.format("User %s has bet %d!", message.getAuthor().getAsMention(), bet))
                    .queue();
        }

        private void deleteMessage(Message message) {
            message.delete()
                    .queue();
        }
    }
}

package org.example.gamble.interaction.gamble;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.interaction.base.TimeRemainingInteraction;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CollectGamblersInteraction extends Interaction<MessageReceivedEvent, List<User>> {

    private static final Predicate<String> GAMBLE_ENTRY_MESSAGE_PREDICATE = Pattern.compile("^!gamble \\d+$")
            .asMatchPredicate();

    private final Interaction<TextChannel, Void> timeRemainingNotificationInteraction =
            new TimeRemainingInteraction(Collections.singletonList(Duration.ofSeconds(10)));

    @Override
    public CompletableFuture<List<User>> apply(MessageReceivedEvent input) {
        final TextChannel targetChannel = input.getChannel().asTextChannel();
        final JDA jda = input.getJDA();
        final GamblerEntryMessageListener entryMessageListener = new GamblerEntryMessageListener();

        jda.addEventListener(entryMessageListener);
        return timeRemainingNotificationInteraction.apply(targetChannel)
                .thenAccept($ -> jda.removeEventListener(entryMessageListener))
                .thenCompose($ -> notifyCollectionEnded(targetChannel))
                .thenApply($ -> entryMessageListener.getGamblers());
    }

    private CompletableFuture<Message> notifyCollectionEnded(TextChannel targetChannel) {
        return targetChannel.sendMessage("Gamblers registration closed!")
                .submit();
    }

    private static class GamblerEntryMessageListener extends ListenerAdapter {

        @Getter
        private final List<User> gamblers = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void onMessageReceived(@NotNull MessageReceivedEvent event) {
            if (GAMBLE_ENTRY_MESSAGE_PREDICATE.test(event.getMessage().getContentRaw())) {
                final User gambler = event.getAuthor();

                gamblers.add(gambler);

                event.getChannel()
                        .sendMessage(String.format("User %s will participate in gamble!", gambler))
                        .queue();
            }
        }
    }
}

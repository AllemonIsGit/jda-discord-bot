package org.example.gamble.interaction.base;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TimeRemainingInteraction extends Interaction<TextChannel, Void> {

    private final List<Duration> durations;

    @Override
    public CompletableFuture<Void> apply(TextChannel input) {
        return durations.stream()
                .map(duration -> createDurationNotification(duration, input))
                .reduce(CompletableFuture.completedFuture(null), (next, acc) -> acc.thenCompose($ -> next));
    }

    private CompletableFuture<Void> createDurationNotification(Duration duration, TextChannel textChannel) {
        return textChannel.sendMessage(String.format("Seconds remaining: %d", duration.getSeconds()))
                .submit()
                .thenAccept($ -> trySleep(duration.toMillis()));
    }

    private void trySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
}

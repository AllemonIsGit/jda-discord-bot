package org.example.gamble.interaction.base;

import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
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
        return getNotificationTasks(input)
                .fold(CompletableFuture.completedFuture(null), this::executeNext);
    }

    private Stream<CompletableFuture<Void>> getNotificationTasks(TextChannel channel) {
        final Seq<Duration> durationsToWait = Stream.ofAll(durations)
                .toList();

        final Seq<Duration> durationsToNotify = Stream.iterate(durationsToWait, Seq::tail)
                .takeWhile(Seq::nonEmpty)
                .map(remaining -> remaining.reduce(Duration::plus));

        return durationsToWait
                .zipWith(durationsToNotify, (toWait, toNotify) -> getNotificationTask(toWait, toNotify, channel))
                .toStream();
    }

    private CompletableFuture<Void> getNotificationTask(Duration toWait, Duration toNotify, TextChannel textChannel) {
        return textChannel.sendMessage("Seconds remaining: " + toNotify.getSeconds())
                .submit()
                .thenAccept($ -> trySleep(toWait.toMillis()));
    }

    private CompletableFuture<Void> executeNext(CompletableFuture<Void> first, CompletableFuture<Void> second) {
        return first.thenCompose($ -> second);
    }

    private void trySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
}

package org.example.gamble.interaction.base;

import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.gamble.utils.Futures;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TimeRemainingInteraction extends Interaction<TextChannel, Void> {

    private final List<Duration> durations;

    @Override
    public CompletableFuture<Void> apply(TextChannel input) {
        return Futures.sequence(createNotifications(input), Notification::getFuture);
    }

    private Stream<Notification> createNotifications(TextChannel channel) {
        final Seq<Duration> durationsToWait = Stream.ofAll(durations)
                .toList();

        final Seq<Duration> durationsToNotify = Stream.iterate(durationsToWait, Seq::tail)
                .takeWhile(Seq::nonEmpty)
                .map(remaining -> remaining.reduce(Duration::plus));

        return durationsToWait
                .zipWith(durationsToNotify, (toWait, toNotify) -> new Notification(toWait, toNotify, channel))
                .toStream();
    }

    private record Notification(Duration toWait, Duration toNotify, TextChannel textChannel) {
        public CompletableFuture<Void> getFuture() {
            return textChannel.sendMessage("Seconds remaining: " + toNotify.getSeconds()).submit()
                    .thenCompose($ -> Futures.waitMillis(toWait.toMillis()));
        }
    }
}

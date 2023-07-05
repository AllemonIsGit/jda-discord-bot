package org.example.gamble.interaction.gamble;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.gamble.embed.GambleResultEmbed;
import org.example.gamble.embed.PrepareGambleEmbed;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.utils.Futures;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PrepareGambleInteraction implements Interaction<SlashCommandInteractionEvent, GambleResultEmbed> {

    private static final Duration TOTAL_DURATION = Duration.ofSeconds(70);

    private static final Duration WAIT_PER_ITERATION = Duration.ofSeconds(2);

    @Override
    public CompletableFuture<GambleResultEmbed> apply(SlashCommandInteractionEvent event) {
        final JDA jda = event.getJDA();
        final PrepareGambleEmbed prepareGambleEmbed = new PrepareGambleEmbed(event);
        final PlaceBetMessageListener placeBetListener = new PlaceBetMessageListener(prepareGambleEmbed);

        jda.addEventListener(placeBetListener);
        return notifyPreparationStarted(event)
                .thenAccept($ -> prepareGambleEmbed.setTimeRemaining(TOTAL_DURATION))
                .thenCompose($ -> performCountdown(prepareGambleEmbed))
                .thenAccept($ -> jda.removeEventListener(placeBetListener))
                .thenApply($ -> prepareGambleEmbed.finishPreparation());
    }

    private CompletableFuture<Void> notifyPreparationStarted(SlashCommandInteractionEvent event) {
        return event.reply("Gamble started!")
                .setEphemeral(true)
                .submit()
                .thenCompose(Futures.discardResult());
    }

    private CompletableFuture<Void> performCountdown(PrepareGambleEmbed embed) {
        final List<Duration> remainingDurations = Stream.iterate(
                        TOTAL_DURATION,
                        duration -> !duration.isNegative(),
                        duration -> duration.minus(WAIT_PER_ITERATION))
                .toList();

        return Futures.sequence(remainingDurations, remaining -> waitAndUpdate(remaining, embed));
    }

    private CompletableFuture<Void> waitAndUpdate(Duration remaining, PrepareGambleEmbed embed) {
        return Futures.waitMillis(WAIT_PER_ITERATION.toMillis())
                .thenAccept($ -> embed.setTimeRemaining(remaining));
    }
}

package org.example.gamble.interaction.gamble;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.gamble.Gamble;
import org.example.gamble.WeightedRandomizer;
import org.example.gamble.embed.GambleResultEmbed;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.utils.Futures;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class PerformGambleInteraction implements Interaction<SlashCommandInteractionEvent, Void> {

    private static final Duration ANNOUCE_WINNER_DURATION = Duration.ofSeconds(3);

    private final WeightedRandomizer randomizer = new WeightedRandomizer();

    private final Interaction<SlashCommandInteractionEvent, GambleResultEmbed> prepareGambleInteraction;

    public PerformGambleInteraction() {
        this(new PrepareGambleInteraction());
    }

    @Override
    public CompletableFuture<Void> apply(SlashCommandInteractionEvent event) {
        return prepareGambleInteraction.apply(event)
                .thenCompose(this::performGamble);
    }

    private CompletableFuture<Void> performGamble(GambleResultEmbed resultEmbed) {
        return resultEmbed.update()
                .thenCompose($ -> selectWinner(resultEmbed));
    }

    private CompletableFuture<Void> selectWinner(GambleResultEmbed activeGambleEmbed) {
        final Gamble gamble = activeGambleEmbed.getGamble();
        final User winner = randomizer.pick(gamble.getParticipants(), gamble::getBetForUser);
        return Futures.waitMillis(ANNOUCE_WINNER_DURATION.toMillis())
                .thenAccept($ -> activeGambleEmbed.setWinner(winner));
    }
}

package org.example.gamble.interaction.gamble;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.gamble.Gamble;
import org.example.gamble.WeightedRandomizer;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.embed.ActiveGambleEmbed;
import org.example.gamble.embed.CancelledGambleEmbed;
import org.example.gamble.utils.Futures;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class PerformGambleInteraction implements Interaction<SlashCommandInteractionEvent, Void> {

    private static final Duration ANNOUCE_WINNER_DURATION = Duration.ofSeconds(3);

    private final WeightedRandomizer randomizer = new WeightedRandomizer();

    private final Interaction<SlashCommandInteractionEvent, Either<CancelledGambleEmbed, ActiveGambleEmbed>> prepareGambleInteraction;

    public PerformGambleInteraction() {
        this(new PrepareGambleInteraction());
    }

    @Override
    public CompletableFuture<Void> apply(SlashCommandInteractionEvent event) {
        return prepareGambleInteraction.apply(event)
                .thenCompose(this::performGamble);
    }

    private CompletableFuture<Void> performGamble(Either<CancelledGambleEmbed, ActiveGambleEmbed> resultEmbed) {
        if (resultEmbed.isLeft()) {
            return resultEmbed.getLeft()
                    .update();
        }

        return resultEmbed.get()
                .update()
                .thenCompose($ -> selectWinner(resultEmbed.get()));
    }

    private CompletableFuture<Void> selectWinner(ActiveGambleEmbed activeGambleEmbed) {
        final Gamble gamble = activeGambleEmbed.getGamble();
        final User winner = randomizer.pick(gamble.getParticipants(), gamble::getBetForUser);
        return Futures.waitMillis(ANNOUCE_WINNER_DURATION.toMillis())
                .thenAccept($ -> activeGambleEmbed.setWinner(winner));
    }
}

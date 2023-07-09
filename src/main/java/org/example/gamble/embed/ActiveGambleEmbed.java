package org.example.gamble.embed;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.gamble.Gamble;
import org.example.gamble.utils.Futures;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ActiveGambleEmbed extends UpdatableEmbed implements GambleResultEmbed {

    private static final Duration ANNOUCE_WINNER_DURATION = Duration.ofSeconds(3);

    @Getter
    private final Gamble gamble;

    private User winner;

    public ActiveGambleEmbed(TextChannel targetChannel, Message embedMessage, Gamble gamble) {
        super(targetChannel, embedMessage);
        this.gamble = gamble;
    }

    @Override
    protected MessageEmbed createEmbed() {
        return new GambleEmbedBuilder()
                .withInitiator(gamble.getInitiator())
                .withLeaderboard(gamble.getLeaderboard())
                .withTotalBet(gamble.getTotalBet())
                .withWinner(winner)
                .build();
    }

    @Override
    public CompletableFuture<Void> prepare() {
        return update();
    }

    public CompletableFuture<Void> announceWinner(User winner) {
        this.winner = winner;
        return Futures.waitMillis(ANNOUCE_WINNER_DURATION.toMillis())
                .thenCompose($ -> update());
    }
}

package org.example.gamble.embed;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.gamble.Gamble;

import java.util.concurrent.CompletableFuture;

public class CancelledGambleEmbed extends UpdatableEmbed implements GambleResultEmbed {

    @Getter
    private final Gamble gamble;

    public CancelledGambleEmbed(TextChannel targetChannel, Message embedMessage, Gamble gamble) {
        super(targetChannel, embedMessage);
        this.gamble = gamble;
    }

    @Override
    protected MessageEmbed createEmbed() {
        return new GambleEmbedBuilder()
                .withInitiator(gamble.getInitiator())
                .withLeaderboard(gamble.getLeaderboard())
                .withTotalBet(gamble.getTotalBet())
                .withCancellation("Gamble cancelled.")
                .build();
    }

    @Override
    public CompletableFuture<Void> prepare() {
        return update();
    }

    @Override
    public CompletableFuture<Void> announceWinner(User winner) {
        return CompletableFuture.completedFuture(null);
    }
}

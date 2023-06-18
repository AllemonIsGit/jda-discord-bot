package org.example.gamble.embed;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.gamble.Gamble;

public class CancelledGambleEmbed extends UpdatableEmbed {

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
}

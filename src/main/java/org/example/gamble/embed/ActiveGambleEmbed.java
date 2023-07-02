package org.example.gamble.embed;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.gamble.Gamble;

public class ActiveGambleEmbed extends UpdatableEmbed implements GambleResultEmbed {

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

    public void setWinner(User winner) {
        this.winner = winner;
        update();
    }
}

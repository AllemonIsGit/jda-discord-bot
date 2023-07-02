package org.example.gamble.embed;

import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.gamble.Gamble;

import java.time.Duration;

public class PrepareGambleEmbed extends UpdatableEmbed {

    @Getter
    private final Gamble gamble;

    private Duration timeRemaining;

    public PrepareGambleEmbed(SlashCommandInteractionEvent event) {
        super(event.getChannel().asTextChannel());
        this.gamble = new Gamble(event.getUser());
    }

    @Override
    protected MessageEmbed createEmbed() {
        return new GambleEmbedBuilder()
                .withInitiator(gamble.getInitiator())
                .withLeaderboard(gamble.getLeaderboard())
                .withTotalBet(gamble.getTotalBet())
                .withTimeRemaining(timeRemaining)
                .build();
    }

    public boolean addParticipant(User user, int bet) {
        final boolean result = gamble.addGambler(user, bet);
        update();
        return result;
    }

    public void setTimeRemaining(Duration timeRemaining) {
        this.timeRemaining = timeRemaining;
        update();
    }

    public GambleResultEmbed finishPreparation() {
        return gamble.getParticipants().isEmpty() ?
                new CancelledGambleEmbed(targetChannel, embedMessage, gamble) :
                new ActiveGambleEmbed(targetChannel, embedMessage, gamble);
    }
}

package org.example.gamble.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.example.gamble.Emojis;
import org.example.gamble.Gamble;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GambleEmbedBuilder {

    private final EmbedBuilder embedBuilder;

    public GambleEmbedBuilder() {
        embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x388227)
                .setTitle("Gamble!");
    }

    public GambleEmbedBuilder withInitiator(User initiator) {
        final String iniatorString = initiator.getAsMention();
        embedBuilder.addField("Initiator", iniatorString, false);
        return this;
    }

    public GambleEmbedBuilder withLeaderboard(List<Gamble.Participant> leaderboard) {
        final String participantsString = leaderboard
                .stream()
                .map(participant -> participant.user().getAsMention() + " " + participant.bet())
                .collect(Collectors.joining("\n"));

        embedBuilder.addField("Participants", participantsString, true);
        return this;
    }

    public GambleEmbedBuilder withTotalBet(long totalBet) {
        final String totalBetString = Long.toString(totalBet);
        embedBuilder.addField("Total bet", totalBetString, true);
        return this;
    }

    public GambleEmbedBuilder withTimeRemaining(@Nullable Duration timeRemaining) {
        final String timeRemainingString = Optional.ofNullable(timeRemaining)
                .map(Duration::toSeconds)
                .map(longSeconds -> longSeconds.intValue() / 2)
                .map("."::repeat)
                .orElse("");

        embedBuilder.addField("Time remaining", timeRemainingString, false);
        return this;
    }

    public GambleEmbedBuilder withWinner(@Nullable User winner) {
        final String winnerString = Optional.ofNullable(winner)
                .map(user -> user.getAsMention() + " " + Emojis.TADA)
                .orElse("");

        embedBuilder.addField("Winner is:", winnerString, false);
        return this;
    }

    public GambleEmbedBuilder withCancellation(String message) {
        embedBuilder.addField(message, Emojis.POOP, false);
        return this;
    }

    public MessageEmbed build() {
        return embedBuilder.build();
    }
}

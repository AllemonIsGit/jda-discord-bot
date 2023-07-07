package org.example.gamble.interaction.gamble;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.example.gamble.SlashCommands;
import org.example.gamble.embed.PrepareGambleEmbed;
import org.example.service.GuildUserService;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
public class PlaceBetMessageListener extends ListenerAdapter {


    private final PrepareGambleEmbed prepareGambleEmbed;
    private final GuildUserService guildUserService;

    public PlaceBetMessageListener(PrepareGambleEmbed prepareGambleEmbed) {
        this.prepareGambleEmbed = prepareGambleEmbed;
        this.guildUserService = GuildUserService.getInstance();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isInTargetChannel(event.getChannel())) {
            return;
        }

        final String commandName = event.getFullCommandName();
        if (!SlashCommands.PLACE_BET_COMMAND_NAME.equals(commandName)) {
            return;
        }

        final Optional<Integer> parsedBet = extractBet(event);
        if (parsedBet.isEmpty()) {
            replyEphemeral(event, "Sorry, provided bet is invalid.");
            return;
        }

        final int bet = parsedBet.get();

        if (!canBet(event, bet)) {
            return;
        }

        guildUserService.revokePoints(event.getUser().getId(), (long) bet);

        if (prepareGambleEmbed.addParticipant(event.getUser(), bet)) {
            replyEphemeral(event, "Bet " + bet + " placed!");
        } else {
            replyEphemeral(event, "Provided bet has to be greater than already placed.");
        }
    }

    private boolean canBet(SlashCommandInteractionEvent event, Integer bet) {
        User user = event.getUser();
        double currentBet = prepareGambleEmbed.getGamble().getBetForUser(user);
        Long userPoints = guildUserService.getPointsBySnowflakeId(user.getId());
        double availablePoints = userPoints - (int) currentBet;
        if (availablePoints < bet) {
            replyEphemeral(event,"You don't have enough points.");
            return false;
        }
        return true;
    }

    private boolean isInTargetChannel(Channel messageChannel) {
        return prepareGambleEmbed.getTargetChannel().getId()
                .equals(messageChannel.getId());
    }

    private Optional<Integer> extractBet(SlashCommandInteractionEvent event) {
        try {
            final Integer bet = event.getOption("amount", OptionMapping::getAsInt);
            return Optional.ofNullable(bet)
                    .filter(amount -> amount > 0);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void replyEphemeral(SlashCommandInteractionEvent event, String reply) {
        event.reply(reply)
                .setEphemeral(true)
                .queue();
    }
}

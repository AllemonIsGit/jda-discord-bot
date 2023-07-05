package org.example.gamble;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.gamble.interaction.base.ActionNotAllowedInteraction;
import org.example.gamble.interaction.base.Interaction;
import org.example.gamble.interaction.base.SomethingWentWrongInteraction;
import org.example.gamble.interaction.gamble.PerformGambleInteraction;
import org.example.gamble.utils.Futures;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class GambleListener extends ListenerAdapter {

    private final Locker<Channel> channelLocker = new Locker<>(Comparator.comparing(Channel::getId));

    private final Interaction<SlashCommandInteractionEvent, Void> errorInteraction;

    private final Interaction<SlashCommandInteractionEvent, Void> actionNotAllowedInteraction;

    private final Interaction<SlashCommandInteractionEvent, Void> performGambleInteraction;

    public GambleListener() {
        this.errorInteraction = new SomethingWentWrongInteraction();
        this.actionNotAllowedInteraction = new ActionNotAllowedInteraction();
        this.performGambleInteraction = new PerformGambleInteraction();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild()
                .updateCommands()
                .addCommands(SlashCommands.INIT_GAMBLE, SlashCommands.PLACE_BET)
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        val targetChannel = event.getChannel().asTextChannel();

        switch (event.getFullCommandName()) {
            case SlashCommands.INIT_GAMBLE_COMMAND_NAME -> onGambleInit(event, targetChannel);
            case SlashCommands.PLACE_BET_COMMAND_NAME -> onPlaceBet(event, targetChannel);
        }
    }

    private void onGambleInit(@NotNull SlashCommandInteractionEvent event, TextChannel targetChannel) {
        if (channelLocker.isLocked(targetChannel)) {
            actionNotAllowedInteraction.apply(event).resultNow();
            return;
        }

        channelLocker.lock(targetChannel);
        performGambleInteraction.apply(event)
                .whenComplete(($, e) -> channelLocker.unlock(targetChannel))
                .exceptionallyCompose(error -> reportException(error, event));
    }

    private void onPlaceBet(@NotNull SlashCommandInteractionEvent event, TextChannel targetChannel) {
        if (!channelLocker.isLocked(targetChannel)) {
            actionNotAllowedInteraction.apply(event).resultNow();
        }
    }

    private CompletableFuture<Void> reportException(Throwable throwable, SlashCommandInteractionEvent event) {
        System.err.println(getClass().getSimpleName() + ": " + throwable.getMessage());
        throwable.printStackTrace();
        return errorInteraction.apply(event);
    }
}

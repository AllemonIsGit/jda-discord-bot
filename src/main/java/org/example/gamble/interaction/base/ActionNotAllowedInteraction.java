package org.example.gamble.interaction.base;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.gamble.utils.Futures;

import java.util.concurrent.CompletableFuture;

public class ActionNotAllowedInteraction implements Interaction<SlashCommandInteractionEvent, Void> {
    @Override
    public CompletableFuture<Void> apply(SlashCommandInteractionEvent event) {
        return event.reply("You can't do this now.")
                .setEphemeral(true)
                .submit()
                .thenCompose(Futures.discardResult());
    }
}

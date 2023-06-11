package org.example.gamble.interaction.base;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.example.gamble.utils.Futures;

import java.util.concurrent.CompletableFuture;

public class ErrorInteraction extends Interaction<TextChannel, Void> {
    @Override
    public CompletableFuture<Void> apply(TextChannel input) {
        return input.sendMessage("Something exploded. :robot:")
                .submit()
                .thenCompose(Futures.asVoid());
    }
}

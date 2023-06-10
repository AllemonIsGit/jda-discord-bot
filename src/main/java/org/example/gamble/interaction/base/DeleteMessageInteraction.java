package org.example.gamble.interaction.base;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.CompletableFuture;

public class DeleteMessageInteraction extends Interaction<MessageReceivedEvent, Void> {

    @Override
    public CompletableFuture<Void> apply(MessageReceivedEvent event) {
        return event.getMessage()
                .delete()
                .submit();
    }
}

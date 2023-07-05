package org.example.gamble.embed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@RequiredArgsConstructor
abstract class UpdatableEmbed {

    @Getter
    protected final TextChannel targetChannel;

    protected Message embedMessage;

    public CompletableFuture<Void> update() {
        return embedMessage == null ? sendNewMessage() : updateMessage();
    }

    private CompletableFuture<Void> sendNewMessage() {
        return targetChannel.sendMessage(MessageCreateData.fromEmbeds(createEmbed()))
                .submit()
                .thenAccept(message -> embedMessage = message);
    }

    private CompletableFuture<Void> updateMessage() {
        return embedMessage.editMessage(MessageEditData.fromEmbeds(createEmbed()))
                .submit()
                .thenAccept(message -> embedMessage = message);
    }

    protected abstract MessageEmbed createEmbed();
}

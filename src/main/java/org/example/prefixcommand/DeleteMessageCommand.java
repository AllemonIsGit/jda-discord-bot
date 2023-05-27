package org.example.prefixcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DeleteMessageCommand implements TextCommand {
    String invokePhrase = "delete";

    @Override
    public String getInvokePhrase() {
        return invokePhrase;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String messageId = event.getChannel().getLatestMessageId();
        event.getChannel().deleteMessageById(messageId).queue();
    }
}

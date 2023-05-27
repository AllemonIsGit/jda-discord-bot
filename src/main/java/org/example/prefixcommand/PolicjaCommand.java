package org.example.prefixcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PolicjaCommand implements TextCommand {
    String invokePhrase = "policja";

    @Override
    public String getInvokePhrase() {
        return invokePhrase;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Jebać policje!").queue();
    }
}

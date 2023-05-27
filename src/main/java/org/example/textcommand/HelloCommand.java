package org.example.textcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelloCommand implements TextCommand {
    String invokePhrase = "hello";

    @Override
    public String getInvokePhrase() {
        return invokePhrase;
    }
    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Hello World!").queue();
    }
}

package org.example.textcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface TextCommand {
    String getInvokePhrase();
    void execute(MessageReceivedEvent event);
}

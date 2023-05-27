package org.example.textcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.service.InsultService;

public class InsultCommand implements TextCommand {
    private final String invokePhrase = "insultme";
    @Override
    public String getInvokePhrase() {
        return invokePhrase;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        InsultService.insult(event);
    }
}

package org.example.textcommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.service.InsultService;

public class InsultCommand implements TextCommand {
    private final String invokePhrase = "insultme";
    private final InsultService insultService;

    public InsultCommand(InsultService insultService) {
        this.insultService = insultService;
    }
    @Override
    public String getInvokePhrase() {
        return invokePhrase;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        insultService.insult(event);
    }
}

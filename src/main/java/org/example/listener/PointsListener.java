package org.example.listener;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.service.GuildUserService;
import org.example.util.MessageCache;
import org.example.util.SimpleMessage;

public class PointsListener extends ListenerAdapter {
    private final GuildUserService guildUserService;
    private final MessageCache messageCache;

    public PointsListener() {
        this.guildUserService = GuildUserService.getInstance();
        this.messageCache = MessageCache.getInstance();
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guildUserService.awardPoints(event);
        SimpleMessage message = new SimpleMessage(event.getMessageId(), event.getAuthor().getId());
        messageCache.add(message);
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        guildUserService.revokePoints(event);
    }


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        guildUserService.awardPoints(event);
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        guildUserService.revokePoints(event);
    }


}

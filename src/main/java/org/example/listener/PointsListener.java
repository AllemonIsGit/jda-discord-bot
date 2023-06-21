package org.example.listener;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.service.GuildUserService;

public class PointsListener extends ListenerAdapter {
    private final GuildUserService guildUserService;

    public PointsListener() {
        this.guildUserService = GuildUserService.getInstance();
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guildUserService.awardPoints(event);
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        super.onMessageDelete(event);
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

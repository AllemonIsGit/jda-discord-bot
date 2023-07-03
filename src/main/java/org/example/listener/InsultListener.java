package org.example.listener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.manager.SlashCommandManager;
import org.example.service.EventService;
import org.example.service.InsultService;
import org.example.slashcommand.SlashCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public final class InsultListener extends Listener {
    private final InsultService insultService;
    private Integer insultChance = 2;

    public InsultListener() {
        this.slashCommandManager = SlashCommandManager.getInstance();
        this.slashCommands = slashCommandManager.getInsultSlashCommands();
        this.slashCommandData = slashCommandManager.getInsultSlashCommandsData();

        this.eventService = EventService.getInstance();
        this.insultService = InsultService.getInstance();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Optional<List<String>> message = eventService.handleMessageEvent(event);

        if (message.isEmpty()) {
            rollInsult(event);
        }
    }

    private void rollInsult(@NotNull MessageReceivedEvent event) {
        Random random = new Random();

        if (random.nextInt(100) <= insultChance) {
            insultService.insult(event);
        }
    }
}

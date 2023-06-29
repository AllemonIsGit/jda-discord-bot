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
import java.util.Random;

public class InsultListener extends ListenerAdapter {
    private final EventService eventService;
    private final InsultService insultService;
    private final SlashCommandManager slashCommandManager;
    private final List<SlashCommand> slashCommands;
    private Integer insultChance = 2;

    public InsultListener() {
        this.slashCommandManager = SlashCommandManager.getInstance();
        slashCommands = slashCommandManager.getInsultSlashCommands();

        this.eventService = EventService.getInstance();
        this.insultService = InsultService.getInstance();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> data = slashCommandManager.getInsultSlashCommandsData();
        data.forEach((e) -> event.getGuild().upsertCommand(e).queue());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        List<String> splitMessage = eventService.splitMessageOnSpace(eventService.getMessageFromEvent(event));

        if (!eventService.checkForPrefix(splitMessage.get(0)) && !event.getAuthor().isBot()) {
            rollInsult(event);
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getFullCommandName();
        for (SlashCommand slashCommand : slashCommands) {
            if (Objects.equals(command, slashCommand.getData().getName())) {
                slashCommand.execute(event);
            }
        }
    }

    private void rollInsult(@NotNull MessageReceivedEvent event) {
        Random random = new Random();

        if (random.nextInt(100) <= insultChance) {
            insultService.insult(event);
        }
    }
}

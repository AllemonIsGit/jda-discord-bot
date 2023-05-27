package org.example.listener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.manager.SlashCommandManager;
import org.example.manager.TextCommandManager;
import org.example.textcommand.TextCommand;
import org.example.service.EventService;
import org.example.slashcommand.SlashCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class GlobalListener extends ListenerAdapter {
    private final TextCommandManager textCommandManager;
    private final SlashCommandManager slashCommandManager;
    private final List<TextCommand> textCommands;
    private final List<SlashCommand> slashCommands;
    private final EventService eventService;

    public GlobalListener() {
        this.eventService = new EventService();

        this.textCommandManager = new TextCommandManager();
        this.textCommands = textCommandManager.getTextCommands();

        this.slashCommandManager = new SlashCommandManager();
        this.slashCommands = slashCommandManager.getSlashCommands();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getFullCommandName();
        for (SlashCommand slashCommand : slashCommands) {
            if (Objects.equals(command, slashCommand.getData().getName())) {
                slashCommand.execute(event);
            }
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        for (SlashCommand slashCommand : slashCommands) {
            event.getGuild()
                    .updateCommands()
                    .addCommands(slashCommand.getData())
                    .queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        List<String> splitMessage = eventService.splitMessageOnSpace(eventService.getMessageFromEvent(event));

        if (!eventService.checkForPrefix(splitMessage.get(0))) {
            return;
        }

        splitMessage = eventService.removePrefix(splitMessage);

        for (TextCommand command: textCommands) {
            if (splitMessage.get(0).equalsIgnoreCase(command.getInvokePhrase())) {
                command.execute(event);
            }
        }
    }
}

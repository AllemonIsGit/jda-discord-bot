package org.example.listener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.manager.SlashCommandManager;
import org.example.manager.TextCommandManager;
import org.example.service.EventService;
import org.example.slashcommand.SlashCommand;
import org.example.textcommand.TextCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class GlobalListener extends Listener {
    private final TextCommandManager textCommandManager;
    private final List<TextCommand> textCommands;

    public GlobalListener() {
        this.eventService = EventService.getInstance();

        this.slashCommandManager = SlashCommandManager.getInstance();
        this.slashCommands = slashCommandManager.getGenericSlashCommands();
        this.slashCommandData = slashCommandManager.getGenericSlashCommandsData();

        this.textCommandManager = TextCommandManager.getInstance();
        this.textCommands = textCommandManager.getTextCommands();
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Optional<List<String>> optionalMessage = eventService.handleMessageEvent(event);

        optionalMessage.ifPresent((message) -> {
            for (TextCommand command: textCommands) {
                if (message.get(0).equalsIgnoreCase(command.getInvokePhrase())) {
                    command.execute(event);
                }
            }
        });
    }
}

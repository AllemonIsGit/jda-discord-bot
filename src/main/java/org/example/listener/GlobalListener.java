package org.example.listener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.manager.SlashCommandManager;
import org.example.manager.TextCommandManager;
import org.example.prefixcommand.TextCommand;
import org.example.service.EventService;
import org.example.slashcommand.SlashCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        for (SlashCommand slashCommand : slashCommands) {
            event.getGuild().updateCommands().addCommands(slashCommand.getData()).queue();
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

//    private String getMessageFromEvent(MessageReceivedEvent event) {
//        return event.getMessage().getContentRaw();
//    }
//
//    private Boolean checkForPrefix(String message) {
//        return message.startsWith(prefix);
//    }
//
//    private List<String> splitMessageOnSpace(String message) {
//        String[] splitMessage = message.split(" ");
//       return new ArrayList<>(Arrays.asList(splitMessage));
//    }
//
//    private List<String> removePrefix(List<String> message) {
//        String firstString = message.get(0);
//        firstString = firstString.substring(1);
//        message.set(0, firstString);
//        return message;
//    }
}

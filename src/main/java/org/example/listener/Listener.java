package org.example.listener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.manager.SlashCommandManager;
import org.example.service.EventService;
import org.example.slashcommand.SlashCommand;

import java.util.List;
import java.util.Objects;

public abstract class Listener extends ListenerAdapter {
    protected SlashCommandManager slashCommandManager;
    protected EventService eventService;
    protected List<CommandData> slashCommandData;
    protected List<SlashCommand> slashCommands;

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        slashCommandData.forEach((e) -> event.getGuild().upsertCommand(e).queue());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        slashCommands.stream()
                .filter((e) -> Objects.equals(
                        e.getData().getName(), event.getFullCommandName()))
                .findFirst()
                .ifPresent((e) -> e.execute(event));
    }
}

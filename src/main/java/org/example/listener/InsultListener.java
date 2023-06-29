package org.example.listener;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.service.EventService;
import org.example.service.InsultService;
import org.example.slashcommand.SlashConsent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class InsultListener extends ListenerAdapter {
    private final SlashConsent slashConsent;
    private final EventService eventService;
    private final InsultService insultService;
    private Integer insultChance = 2;

    public InsultListener() {
        this.eventService = EventService.getInstance();
        this.insultService = InsultService.getInstance();
        this.slashConsent = new SlashConsent();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().upsertCommand(slashConsent.getData()).queue();
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
        if (Objects.equals(command, slashConsent.getData().getName())) {
            slashConsent.execute(event);
        }
    }

    private void rollInsult(@NotNull MessageReceivedEvent event) {
        Random random = new Random();

        if (random.nextInt(100) <= insultChance) {
            insultService.insult(event);
        }
    }
}

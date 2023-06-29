package org.example.manager;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.slashcommand.*;
import org.example.slashcommand.music.*;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager {
    private static SlashCommandManager INSTANCE;
    @Getter
    private final List<SlashCommand> genericSlashCommands = new ArrayList<>();
    @Getter
    private final List<SlashCommand> insultSlashCommands = new ArrayList<>();

    public static SlashCommandManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SlashCommandManager();
            return INSTANCE;
        }
        return INSTANCE;
    }

    private SlashCommandManager() {
        registerGenericCommand(new SlashClear());
        registerGenericCommand(new SlashPlay());
        registerGenericCommand(new SlashSkip());
        registerGenericCommand(new SlashVolume());
        registerGenericCommand(new SlashStop());
        registerGenericCommand(new SlashNowPlaying());

        registerInsultCommand(new SlashAddInsult());
        registerInsultCommand(new SlashDeleteInsult());
        registerInsultCommand(new SlashInsultTarget());
        registerInsultCommand(new SlashConsent());
    }

    private void registerGenericCommand(SlashCommand command) {
        genericSlashCommands.add(command);
    }

    private void registerInsultCommand(SlashCommand command) {
        insultSlashCommands.add(command);
    }

    public List<CommandData> getGenericSlashCommandsData() {
        List<CommandData> data = new ArrayList<>();

        for (SlashCommand slashCommand : genericSlashCommands) {
            data.add(slashCommand.getData());
        }
        return data;
    }

    public List<CommandData> getInsultSlashCommandsData() {
        List<CommandData> data = new ArrayList<>();

        for (SlashCommand slashCommand : insultSlashCommands) {
            data.add(slashCommand.getData());
        }
        return data;
    }
}

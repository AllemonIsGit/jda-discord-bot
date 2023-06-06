package org.example.manager;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.service.InsultService;
import org.example.slashcommand.*;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager {
    private static SlashCommandManager INSTANCE;
    @Getter
    private final List<SlashCommand> slashCommands = new ArrayList<>();

    private SlashCommandManager() {
        register(new SlashClear());
        register(new SlashAddInsult(InsultService.getInstance()));
        register(new SlashPlay());
        register(new SlashSkip());
    }

    private void register(SlashCommand command) {
        slashCommands.add(command);
    }

    public List<CommandData> getSlashCommandsData() {
        List<CommandData> data = new ArrayList<>();

        for (SlashCommand slashCommand : slashCommands) {
            data.add(slashCommand.getData());
        }
        return data;
    }

    public static SlashCommandManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SlashCommandManager();
            return INSTANCE;
        }
        return INSTANCE;
    }
}

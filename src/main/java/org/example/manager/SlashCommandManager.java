package org.example.manager;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.slashcommand.SlashAddInsult;
import org.example.slashcommand.SlashCommand;
import org.example.slashcommand.SlashDelete;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager {
    @Getter
    private final List<SlashCommand> slashCommands = new ArrayList<>();

    public SlashCommandManager() {
        register(new SlashDelete());
        register(new SlashAddInsult());
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
}

package org.example.manager;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.example.service.InsultService;
import org.example.slashcommand.SlashAddInsult;
import org.example.slashcommand.SlashClear;
import org.example.slashcommand.SlashCommand;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager {
    @Getter
    private final List<SlashCommand> slashCommands = new ArrayList<>();

    public SlashCommandManager(InsultService insultService) {
        register(new SlashClear());
        register(new SlashAddInsult(insultService));
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

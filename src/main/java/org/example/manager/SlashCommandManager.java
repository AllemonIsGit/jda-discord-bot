package org.example.manager;

import lombok.Getter;
import org.example.slashcommand.SlashCommand;
import org.example.slashcommand.SlashDelete;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager {
    @Getter
    private final List<SlashCommand> slashCommands = new ArrayList<>();

    public SlashCommandManager() {
        register(new SlashDelete());

    }

    private void register(SlashCommand command) {
        slashCommands.add(command);
    }
}

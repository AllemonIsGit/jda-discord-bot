package org.example.manager;

import org.example.slashcommand.SlashCommand;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandManager {
    private final List<SlashCommand> slashCommands;

    public SlashCommandManager() {
        this.slashCommands = new ArrayList<>();
    }

    private void register(SlashCommand command) {
        slashCommands.add(command);
    }
}

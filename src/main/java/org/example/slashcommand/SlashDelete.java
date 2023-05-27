package org.example.slashcommand;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class SlashDelete implements SlashCommand {
    private final String commandName = "delete";
    private final String commandDesc = "Deletes number of last posted messages";
    private CommandData data;


    public SlashDelete() {
        this.data = new CommandDataImpl(commandName, commandDesc);
    }
    @Override
    public CommandData getData() {
        return data;
    }

    @Override
    public void execute() {

    }
}

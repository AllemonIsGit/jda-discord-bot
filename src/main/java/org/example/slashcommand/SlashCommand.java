package org.example.slashcommand;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommand {
    CommandData getData();
    void execute();
}

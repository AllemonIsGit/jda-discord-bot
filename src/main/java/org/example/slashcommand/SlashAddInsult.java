package org.example.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.service.InsultService;

public class SlashAddInsult implements SlashCommand {
    private final String commandName = "addinsult";
    private final String commandDescription = "adds insult to insult pool";
    private final String optionName = "add";
    private final String optionDescription = "type your insult here";
    private final CommandData data;
    private final InsultService insultService;

    public SlashAddInsult(InsultService insultService) {
        this.insultService = insultService;

        OptionData option = new OptionData(OptionType.STRING, optionName, optionDescription);
        this.data = new CommandDataImpl(commandName, commandDescription).addOptions(option);
    }

    @Override
    public CommandData getData() {
        return this.data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String insult;

        try {
            insult = event.getOption(optionName).getAsString();
        } catch (NullPointerException e) {
            event.reply("Something went wrong.").setEphemeral(true).queue();
            return;
        }
        insultService.register(insult);
        event.reply("Added!").setEphemeral(true).queue();
        event.getChannel().sendMessage("I've got a new insult in my list!").queue();
    }
}

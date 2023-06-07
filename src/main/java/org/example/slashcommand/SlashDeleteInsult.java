package org.example.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.service.InsultService;

public class SlashDeleteInsult implements SlashCommand {
    private String name = "deleteinsult";
    private String description = "Deletes insult from my list.";
    private CommandData data;
    private String optionName = "id";
    private String optionDescription = "Provide ID of the insult you want to delete.";
    private InsultService insultService;

    public SlashDeleteInsult() {
        this.insultService = InsultService.getInstance();

        OptionData option = new OptionData(OptionType.INTEGER, optionName, optionDescription);
        this.data = new CommandDataImpl(name, description).addOptions(option);
    }

    @Override
    public CommandData getData() {
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Integer id = event.getInteraction().getOption(optionName).getAsInt();

        insultService.deleteById(id);

        event.reply("Ok!").setEphemeral(true).queue();
    }
}

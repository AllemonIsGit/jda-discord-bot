package org.example.slashcommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;

public class SlashClear implements SlashCommand {
    private final String commandName = "clear";
    private final String commandDescription = "Deletes number of last posted messages";
    private CommandData data;
    private final String optionName = "amount";
    private final String optionDescription = "amount to be deleted";


    public SlashClear() {
        OptionData option = new OptionData(OptionType.INTEGER, optionName, optionDescription);
        this.data = new CommandDataImpl(commandName, commandDescription).addOptions(option);
    }

    @Override
    public CommandData getData() {
        return this.data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Integer quantity = event.getInteraction().getOption(optionName).getAsInt();

        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("You can't do that.").setEphemeral(true).queue();
            return;
        }

        event.reply("Ok").setEphemeral(true).queue();

        List<Message> messages = event.getChannel().getHistory().retrievePast(quantity).complete();
        for (Message message : messages) {
            message.delete().queue();
        }
    }
}

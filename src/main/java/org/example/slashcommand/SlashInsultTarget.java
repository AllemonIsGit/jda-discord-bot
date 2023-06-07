package org.example.slashcommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.service.InsultService;

public class SlashInsultTarget implements SlashCommand {
    private String name = "insulttarget";
    private String description = "I will insult a user, and keep you out of it.";
    private CommandData data;
    private String optionName = "user";
    private String optionDescription = "Specify a target";
    private InsultService insultService;

    public SlashInsultTarget() {
        this.insultService = InsultService.getInstance();

        OptionData option = new OptionData(OptionType.USER, optionName, optionDescription);
        this.data = new CommandDataImpl(name, description).addOptions(option);
    }

    @Override
    public CommandData getData() {
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getOption(optionName).getAsMember();
        MessageChannelUnion channel = event.getChannel();

        if (member != null) {
            insultService.insult(member, channel);
            event.reply("I hope that user won't find out it was you...").setEphemeral(true).queue();
            return;
        }
        event.reply("Something went wrong.").setEphemeral(true).queue();
    }
}

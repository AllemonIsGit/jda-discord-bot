package org.example.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.domain.model.GuildUser;
import org.example.service.GuildUserService;

public class SlashConsent implements SlashCommand {
    private static final String name = "consent";
    private static final String description = "Toggles whether or not you want to be insulted be the bot.";
    private CommandData data;
    private static final String optionName = "consent";
    private static final String optionDescription = "Do you want to be insulted or not?";
    private final GuildUserService guildUserService;

    public SlashConsent() {
        this.guildUserService = GuildUserService.getInstance();
        OptionData optionData = new OptionData(OptionType.BOOLEAN, optionName, optionDescription);
        this.data = new CommandDataImpl(name, description).addOptions(optionData);
    }

    @Override
    public CommandData getData() {
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Boolean consent = event.getOption(optionName).getAsBoolean();

        GuildUser guildUser = guildUserService.getGuildUserBySnowflakeId(event.getMember().getId());

        guildUser.setInsultConsent(consent);
        guildUserService.update(guildUser);

        if (consent) {
            event.reply("I will randomly insult you from now on!").setEphemeral(true).queue();
        }
        if (!consent) {
            event.reply("I'm sorry if i offended you, i won't do it from now on.").setEphemeral(true).queue();
        }
    }
}

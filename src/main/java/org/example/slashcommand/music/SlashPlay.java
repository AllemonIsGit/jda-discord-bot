package org.example.slashcommand.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.manager.PlayerManager;
import org.example.slashcommand.SlashCommand;

public class SlashPlay implements SlashCommand {
    private String commandName = "play";
    private String commandDescription = "Plays provided Youtube video (audio only)";
    private CommandData data;
    private String optionName = "link";
    private String optionDescription = "provide Youtube ULR";

    public SlashPlay() {
        OptionData option = new OptionData(OptionType.STRING, optionName, optionDescription);
        this.data = new CommandDataImpl(commandName, commandDescription).addOptions(option);
    }

    @Override
    public CommandData getData() {
        return this.data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel.").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfState = self.getVoiceState();

        if (!selfState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if (selfState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("I'm in a different channel right now.").setEphemeral(true).queue();
                return;
            }
        }

        event.reply("Ok!").setEphemeral(true).queue();

        PlayerManager playerManager = PlayerManager.getInstance();
        playerManager.play(event.getGuild(), event.getOption("link").getAsString());
    }
}

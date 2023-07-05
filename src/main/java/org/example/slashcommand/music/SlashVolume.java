package org.example.slashcommand.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.manager.GuildMusicManager;
import org.example.manager.PlayerManager;
import org.example.slashcommand.SlashCommand;

public class SlashVolume implements SlashCommand {
    private static final String name = "volume";
    private static final String description = "Changes my player volume from 1% - 100%";
    private CommandData data;
    private static final String optionName = "amount";
    private static final String optionDescription = "number the set the volume to";

    public SlashVolume() {
        OptionData optionData = new OptionData(OptionType.INTEGER, optionName, optionDescription);
        this.data = new CommandDataImpl(name, description).addOptions(optionData);
    }
    @Override
    public CommandData getData() {
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in the same voice channel as me.").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            event.reply("I'm not playing a song/video in a voice channel right now.").setEphemeral(true).queue();
            return;
        }

        if (selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.reply("You can't change the volume if you're not listening to me.").setEphemeral(true).queue();
            return;
        }

        checkOptionAndApply(event);
    }

    private void checkOptionAndApply(SlashCommandInteractionEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.getTrackScheduler().getPlayer();

        Integer volume = event.getOption(optionName).getAsInt();

        if (volume < 1) {
            event.reply("Can't go that low, setting to 1").setEphemeral(true).queue();
            player.setVolume(1);
            return;
        }
        if (volume > 100) {
            event.reply("Can't go that high, setting to 100").setEphemeral(true).queue();
            player.setVolume(100);
            return;
        }

        event.reply("Setting to " + volume).setEphemeral(true).queue();
        player.setVolume(volume);
    }
}

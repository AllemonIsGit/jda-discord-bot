package org.example.slashcommand.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.audio.TrackScheduler;
import org.example.manager.GuildMusicManager;
import org.example.manager.PlayerManager;
import org.example.slashcommand.SlashCommand;

public class SlashStop implements SlashCommand {
    private String name = "stop";
    private String description = "Stop me from playing and clears playing queue.";
    private CommandData data;

    public SlashStop() {
        this.data = new CommandDataImpl(name, description);
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
            event.reply("You can't stop me if you're not listening to me.").setEphemeral(true).queue();
            return;
        }

        event.reply("Stopping and clearing the queue.").setEphemeral(true).queue();

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.getQueue().clear();
        trackScheduler.getPlayer().stopTrack();

        event.getGuild().getAudioManager().closeAudioConnection();
    }
}

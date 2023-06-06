package org.example.slashcommand.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.example.manager.GuildMusicManager;
import org.example.manager.PlayerManager;
import org.example.slashcommand.SlashCommand;

import java.awt.*;

public class SlashNowPlaying implements SlashCommand {
    private String name = "nowplaying";
    private String description = "Shows what i'm playing right now";
    private CommandData data;

    public SlashNowPlaying() {
        this.data = new CommandDataImpl(name, description);
    }
    @Override
    public CommandData getData() {
        return this.data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            event.reply("I'm not playing a song/video in a voice channel right now.").setEphemeral(true).queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.getTrackScheduler().getPlayer();

        if (player.getPlayingTrack() == null) {
            event.reply("I'm not playing anything").setEphemeral(true).queue();
            return;
        }

        AudioTrackInfo trackInfo = player.getPlayingTrack().getInfo();

        event.reply("Ok!").setEphemeral(true).queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Requested by: " + event.getUser().getName(), null);
        embedBuilder.setColor(Color.red);
        embedBuilder.setDescription("Now playing:");
        embedBuilder.addField(trackInfo.title, trackInfo.author, false);
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }
}

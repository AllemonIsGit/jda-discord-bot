package org.example.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import org.example.audio.AudioForwarder;
import org.example.audio.TrackScheduler;

@Getter
public class GuildMusicManager {
    private final TrackScheduler trackScheduler;
    private final AudioForwarder audioForwarder;
    private final Guild guild;

    public GuildMusicManager(AudioPlayerManager manager, Guild guild) {
        AudioPlayer player = manager.createPlayer();
        this.trackScheduler = new TrackScheduler(player, guild);

        player.addListener(this.trackScheduler);
        this.audioForwarder = new AudioForwarder(player);

        this.guild = guild;
    }
}

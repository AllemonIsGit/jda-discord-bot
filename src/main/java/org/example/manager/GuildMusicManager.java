package org.example.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import lombok.Getter;
import org.example.audio.AudioForwarder;
import org.example.audio.TrackScheduler;

@Getter
public class GuildMusicManager {
    private final TrackScheduler trackScheduler;
    private final AudioForwarder audioForwarder;

    public GuildMusicManager(AudioPlayerManager manager) {
        AudioPlayer player = manager.createPlayer();
        this.trackScheduler = new TrackScheduler(player);

        player.addListener(this.trackScheduler);
        this.audioForwarder = new AudioForwarder(player);
    }
}

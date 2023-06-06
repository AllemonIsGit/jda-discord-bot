package org.example.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioForwarder implements AudioSendHandler {

    private final AudioPlayer player;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final MutableAudioFrame audioFrame = new MutableAudioFrame();

    public AudioForwarder(AudioPlayer player) {
        this.player = player;
        audioFrame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return player.provide(audioFrame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer.flip();
    }

    // LavaPlayer always provides Opus
    @Override
    public boolean isOpus() {
        return true;
    }
}

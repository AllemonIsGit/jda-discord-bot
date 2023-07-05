package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.listener.GlobalListener;
import org.example.listener.InsultListener;
import org.example.listener.PointsListener;

public class Main {
    private final static String token = System.getenv("BOT_TOKEN");

    public static void main(String[] args) {
        JDA bot = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("for your inputs!"))
                .addEventListeners(new GlobalListener(), new PointsListener(), new InsultListener())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .build();
    }
}
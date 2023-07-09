package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.gamble.GambleListener;
import org.example.listener.GlobalListener;
import org.example.listener.InsultListener;
import org.example.listener.PointsListener;
import org.example.repository.PersistenceConfiguration;

public class Development {

    private static final String TOKEN = System.getenv("BOT_TOKEN");

    private static final String HIBERNATE_CONFIG = "hibernate.h2.cfg.xml";

    public static void main(String[] args) {
        PersistenceConfiguration.getInstance().setHibernateConfigurationFile(HIBERNATE_CONFIG);

        JDA bot = JDABuilder.createDefault(TOKEN)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("for your inputs!"))
                .addEventListeners(new GlobalListener(),
                        new PointsListener(),
                        new InsultListener(),
                        new GambleListener())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .build();
    }
}

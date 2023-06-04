package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.listener.GlobalListener;
import org.example.model.Insult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
    private final static String token = System.getenv("BOT_TOKEN");

    public static void main(String[] args) {
        JDA bot = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("for your inputs!"))
                .addEventListeners(new GlobalListener())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        Insult insult = new Insult("im persisted");

        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(insult);

        transaction.commit();
        session.close();
    }
}
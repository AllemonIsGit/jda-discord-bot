package org.example.repository;

import org.example.domain.exception.GuildUserNotFoundException;
import org.example.domain.model.GuildUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class GuildUserRepository {
    private static GuildUserRepository INSTANCE;
    private final Configuration configuration;
    private final SessionFactory sessionFactory;

    private GuildUserRepository() {
        this.configuration = new Configuration().configure();
        this.sessionFactory = configuration.buildSessionFactory();
    }

    public static GuildUserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuildUserRepository();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public void update(GuildUser user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.merge(user);
        transaction.commit();
        session.close();
    }

    public GuildUser getUserById(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        GuildUser user = session.get(GuildUser.class, id);

        if (user == null) {
            throw new GuildUserNotFoundException("GuildUser was not found in database.");
        }
        return user;
    }

    public GuildUser getUserBySnowflakeId(String id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        GuildUser user = session.createQuery(
                "FROM GuildUser WHERE snowflakeId = :param", GuildUser.class)
                .setParameter("param", id)
                .uniqueResult();

        transaction.commit();
        session.close();

        if (user == null) {
            throw new GuildUserNotFoundException("GuildUser was not found in database.");
        }

        return user;
    }

    public boolean existsBySnowflakeId(String discordId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        GuildUser user = session.createQuery(
                "FROM GuildUser WHERE snowflakeId = :param", GuildUser.class)
                .setParameter("param", discordId)
                .uniqueResult();

        transaction.commit();
        session.close();

        return user != null;
    }

    public void save(GuildUser user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(user);
        transaction.commit();
        session.close();
    }
}

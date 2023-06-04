package org.example.repository;

import org.example.domain.model.Insult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class InsultRepository {
    private static InsultRepository INSTANCE;
    private final Configuration configuration;
    private final SessionFactory sessionFactory;

    private InsultRepository() {
        this.configuration = new Configuration().configure();
        this.sessionFactory = configuration.buildSessionFactory();
    }

    public void save(Insult insult) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(insult);
        transaction.commit();
        session.close();
    }

    public List<Insult> findAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query<Insult> query = session.createQuery("from Insult", Insult.class);
        List<Insult> list = query.getResultList();

        transaction.commit();
        session.close();

        return list;
    }

    public static InsultRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InsultRepository();
            return INSTANCE;
        }
        return INSTANCE;
    }
}

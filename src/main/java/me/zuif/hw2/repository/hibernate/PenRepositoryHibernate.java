package me.zuif.hw2.repository.hibernate;

import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.HibernateSessionFactoryUtil;
import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.repository.ProductRepository;
import me.zuif.hw2.repository.postgres.PenRepositoryDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;
@Singleton
public class PenRepositoryHibernate implements ProductRepository<Pen> {
    private final SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
    private static PenRepositoryHibernate instance;

    @Autowired
    public PenRepositoryHibernate() {
    }

    public static PenRepositoryHibernate getInstance() {
        if (instance == null) {
            instance = new PenRepositoryHibernate();
        }
        return instance;
    }
    @Override
    public void save(Pen pen) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(pen);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveAll(List<Pen> pens) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for(Pen pen : pens) {
            session.save(pen);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Pen> findAll() {
        Session session = sessionFactory.openSession();
        List<Pen> pens= session.createQuery("select pen from Pen pen", Pen.class).getResultList();
        session.close();
        return pens;
    }

    @Override
    public Optional<Pen> findById(String id) {
        Session session = sessionFactory.openSession();
        Optional<Pen> pen = Optional.ofNullable(session.find(Pen.class, id));
        session.close();
        return pen;
    }

    @Override
    public boolean update(Pen pen) {
       try( Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(pen);
            session.getTransaction().commit();
            session.close();
            return true;
        }catch (Exception e) {
           e.printStackTrace();
           return false;
       }
    }

    @Override
    public boolean delete(String id) {
        try( Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(findById(id).get());
            session.getTransaction().commit();
            session.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

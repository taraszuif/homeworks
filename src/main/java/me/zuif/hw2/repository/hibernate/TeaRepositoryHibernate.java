package me.zuif.hw2.repository.hibernate;

import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.HibernateSessionFactoryUtil;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.repository.ProductRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Singleton
public class TeaRepositoryHibernate implements ProductRepository<Tea> {
    private static TeaRepositoryHibernate instance;
    private final SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();

    @Autowired
    public TeaRepositoryHibernate() {

    }

    public static TeaRepositoryHibernate getInstance() {
        if (instance == null) {
            instance = new TeaRepositoryHibernate();
        }
        return instance;
    }

    @Override
    public void save(Tea tea) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(tea);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveAll(List<Tea> teas) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (Tea tea : teas) {
            session.save(tea);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Tea> findAll() {
        Session session = sessionFactory.openSession();
        List<Tea> teas = session.createQuery("select tea from Tea tea", Tea.class).getResultList();
        session.close();
        return teas;
    }

    @Override
    public Optional<Tea> findById(String id) {
        Session session = sessionFactory.openSession();
        Optional<Tea> tea = Optional.ofNullable(session.find(Tea.class, id));
        session.close();
        return tea;
    }

    @Override
    public boolean update(Tea tea) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(tea);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(findById(id).get());
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

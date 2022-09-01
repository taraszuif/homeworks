package me.zuif.hw2.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure(HibernateSessionFactoryUtil.class.getClassLoader().getResource("hibernate.cfg.xml"))
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                sessionFactory.openSession().createQuery()
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                throw e;
            }
        }
        return sessionFactory;
    }
}

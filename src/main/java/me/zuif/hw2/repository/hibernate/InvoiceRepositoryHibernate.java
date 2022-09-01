package me.zuif.hw2.repository.hibernate;

import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.HibernateSessionFactoryUtil;
import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.repository.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class InvoiceRepositoryHibernate implements InvoiceRepository {
    private static InvoiceRepositoryHibernate instance;
    private final SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();

    @Autowired
    public InvoiceRepositoryHibernate() {
    }

    public static InvoiceRepositoryHibernate getInstance() {
        if (instance == null) {
            instance = new InvoiceRepositoryHibernate();
        }
        return instance;
    }

    @Override
    public void save(Invoice invoice) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(invoice);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Optional<Invoice> findById(String id) {
        Session session = sessionFactory.openSession();
        Optional<Invoice> invoice = Optional.ofNullable(session.find(Invoice.class, id));
        session.close();
        return invoice;
    }

    @Override
    public void update(Invoice invoice) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.merge(invoice);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Invoice> findAllGreaterSumInvoices(double sum) {
        Session session = sessionFactory.openSession();
        List<Invoice> greater = session.createQuery("select invoice from Invoice invoice where invoice.sum > :sum",
                Invoice.class).setParameter("sum", sum).getResultList();
        session.close();
        return greater;
    }

    @Override
    public int getInvoiceCount() {
        Session session = sessionFactory.openSession();
        int count = ((Long) session.createQuery("select count(id) from Invoice")
                .getSingleResult()).intValue();
        session.close();
        return count;
    }

    @Override
    public Map<Double, Integer> sortBySum() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Invoice> root = criteriaQuery.from(Invoice.class);

        criteriaQuery.multiselect(criteriaBuilder.count(root.get("id")), root.get("sum"));
        criteriaQuery.groupBy(root.get("sum"));

        TypedQuery<Object[]> query = session.createQuery(criteriaQuery);
        List<Object[]> resultList = query.getResultList();
        Map<Double, Integer> result = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            result.put((Double) objects[1], ((Long) objects[0]).intValue());
        }
        session.close();
        return result;
    }

}

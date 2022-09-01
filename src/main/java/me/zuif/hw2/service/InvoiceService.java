package me.zuif.hw2.service;

import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.repository.InvoiceRepository;
import me.zuif.hw2.repository.hibernate.InvoiceRepositoryHibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class InvoiceService {
    private static InvoiceService instance;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService(InvoiceRepository repository) {
        this.invoiceRepository = repository;
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            instance = new InvoiceService(InvoiceRepositoryHibernate.getInstance());
        }
        return instance;
    }


    public Invoice createFromProducts(List<Product> invoiceProducts) {
        Invoice invoice = new Invoice();

        invoice.setTime(LocalDateTime.now());
        invoice.setSum(invoiceProducts.stream().mapToDouble(Product::getPrice).sum());
        invoice.setProducts(new ArrayList<>(invoiceProducts));
        invoiceRepository.save(invoice);
        return invoice;
    }

    public void updateDate(LocalDateTime newDate, String id) {
        invoiceRepository.findById(id).ifPresentOrElse(invoice -> {
            invoice.setTime(newDate);
            invoiceRepository.update(invoice);
        }, () -> {
            throw new IllegalArgumentException("Unable to update date, invoice with id=" + id + " does not exist!");
        });
    }

    public List<Invoice> findAllGreaterSumInvoices(double sum) {
        return invoiceRepository.findAllGreaterSumInvoices(sum);
    }

    public Optional<Invoice> findById(String id) {
        return invoiceRepository.findById(id);
    }

    public int getInvoiceCount() {
        return invoiceRepository.getInvoiceCount();
    }

    public Map<Double, Integer> sortBySum() {
        return invoiceRepository.sortBySum();
    }

}

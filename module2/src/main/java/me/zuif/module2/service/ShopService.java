package me.zuif.module2.service;

import lombok.Getter;
import lombok.Setter;
import me.zuif.module2.model.Customer;
import me.zuif.module2.model.invoice.Invoice;
import me.zuif.module2.model.invoice.InvoiceType;
import me.zuif.module2.model.product.Product;
import me.zuif.module2.model.product.ProductType;
import me.zuif.module2.model.product.ScreenType;
import me.zuif.module2.model.product.Telephone;
import me.zuif.module2.model.product.television.Country;
import me.zuif.module2.model.product.television.Television;
import me.zuif.module2.util.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShopService {
    private static ShopService instance;
    private final Random RANDOM;
    private final Logger logger;
    private final Function<Map<String, String>, Product> mapToProduct;
    @Getter
    @Setter
    private List<Product> products;
    @Getter
    @Setter
    private List<Invoice> invoices;
    @Setter
    private DoublePredicate predicate;

    private ShopService() {
        RANDOM = new Random();
        products = new ArrayList<>();
        invoices = new ArrayList<>();
        logger = LoggerFactory.getLogger(ShopService.class);

        mapToProduct = (map) -> {
            ProductType type = ProductType.valueOf(map.get("type").toUpperCase());

            return switch (type) {

                case TELEPHONE -> new Telephone(Double.parseDouble(map.getOrDefault("price", String.valueOf(0D))),
                        map.getOrDefault("series", "N/A"),
                        map.getOrDefault("model", "N/A"),
                        ScreenType.valueOf(map.getOrDefault("screen type", ScreenType.UNKNOWN.name())));

                case TELEVISION -> new Television(Double.parseDouble(map.getOrDefault("price", String.valueOf(0D))),
                        Country.valueOf(map.getOrDefault("country", Country.UNKNOWN.name()).toUpperCase()),
                        map.getOrDefault("series", "N/A"),
                        ScreenType.valueOf(map.getOrDefault("screen type", ScreenType.UNKNOWN.name())),
                        Double.parseDouble(map.getOrDefault("diagonal", String.valueOf(0D))));
            };
        };

        predicate = sum -> sum > 1000;
    }

    public static ShopService getInstance() {
        if (instance == null) {
            instance = new ShopService();
        }
        return instance;
    }


    public List<Product> createProducts(InputStream stream) {
        CsvParser parser = CsvParser.getInstance();
        List<Map<String, String>> mapList = parser.getCsvData(stream);
        setProducts(mapList.stream().
                map(m -> mapToProduct.apply(m)).
                collect(Collectors.toList()));
        return products;
    }

    private List<Product> generateRandomProductList() {
        int count = RANDOM.nextInt(1, 6);
        List<Product> randomList = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            randomList.add(products.get(RANDOM.nextInt(products.size())));
        });
        return randomList;
    }

    private InvoiceType getInvoiceType(double invoiceSum) {
        if (predicate.test(invoiceSum)) {
            return InvoiceType.WHOLESALE;
        } else {
            return InvoiceType.RETAIL;
        }
    }

    public double getInvoiceSum(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public Invoice generateRandomInvoice(Customer customer) {
        List<Product> randomProductList = generateRandomProductList();
        InvoiceType invoiceType = getInvoiceType(getInvoiceSum(randomProductList));
        LocalDateTime created = LocalDateTime.now();
        Invoice invoice = new Invoice(randomProductList, customer, invoiceType, created);
        logger.info("[{}] [{}] [{} {}]", created, customer, invoiceType, randomProductList);
        return invoice;
    }

    public long getInvoiceCountByType(InvoiceType type) {
        return invoices.stream()
                .filter(invoice -> invoice.getType().equals(type))
                .count();
    }

    public double getInvoicesSum() {
        return getInvoiceSum(invoices.stream()
                .flatMap(invoice -> invoice.getProductList().stream())
                .toList());
    }

    public Map<Double, Customer> getInvoiceSumAndCustomerInfo() {
        return invoices.stream()
                .collect(Collectors.toMap(invoice -> getInvoiceSum(invoice.getProductList()),
                        Invoice::getCustomer,
                        (p1, p2) -> p1,
                        TreeMap::new));
    }

    public long getSoldProductsCountByType(ProductType productType) {
        return invoices.stream()
                .flatMap(invoice -> invoice.getProductList().stream())
                .filter(product -> product.getType().equals(productType))
                .count();
    }

    public boolean hasOnlyOneProductType(List<Product> products, ProductType productType) {
        return products.stream()
                .allMatch(product -> product.getType().equals(productType));
    }

    public List<Invoice> getInvoicesByCustomersUnderAge(int age, boolean changeType) {
        List<Invoice> list = invoices.stream()
                .filter(invoice -> invoice.getCustomer().getAge() < age)
                .toList();
        if (changeType)
            list.forEach(invoice -> invoice.setType(InvoiceType.LOW_AGE));
        return list;
    }

    public List<Invoice> getFirstInvoices(int count) {
        return invoices.stream()
                .sorted(Comparator.comparing(Invoice::getCreated))
                .limit(count).toList();
    }

    public List<Invoice> getSortedInvoices() {
        Comparator<Invoice> compareAge = Comparator.comparing(invoice -> invoice.getCustomer().getAge(), Comparator.reverseOrder());
        Comparator<Invoice> compareProductsSize = Comparator.comparing(invoice -> invoice.getProductList().size());
        Comparator<Invoice> compareInvoiceSum = Comparator.comparing(invoice -> getInvoiceSum(invoice.getProductList()));
        return invoices.stream().sorted(compareAge.thenComparing(compareProductsSize).thenComparing(compareInvoiceSum)).toList();
    }
}

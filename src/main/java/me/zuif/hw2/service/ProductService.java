package me.zuif.hw2.service;

import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import me.zuif.hw2.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ProductService<T extends Product> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ProductRepository<T> repository;

    public ProductService(ProductRepository<T> repository) {
        this.repository = repository;
    }

    public void createAndSaveProducts(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        List<T> products = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final T product = createProduct();
            products.add(product);
            logger.info(product.getClass().getSimpleName() + " {} has been saved", product.getId());
        }
        repository.saveAll(products);
    }

    protected abstract T createProduct();

    public void save(T product) {
        if (product.getCount() == 0) {
            product.setCount(-1);
        }
        repository.save(product);
    }

    protected ProductRepository<T> getRepository() {
        return repository;
    }

    public void update(Product product) {
        repository.update((T) product);
    }

    public T findById(String id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void printAll() {
        for (T product : repository.findAll()) {
            System.out.println(product);
        }
    }

    public class StreamHomework<T extends Product> {
        //как я понял у продукта нет цены если его цена == 0
        public Predicate<Collection<T>> hasPrice = (products) ->
                products.stream().noneMatch(product -> product.getPrice() == 0);

        public int sumProduct() {
            return repository.findAll().stream().map(Product::getCount).reduce(0, Integer::sum);
        }

        public void printGreaterThanReferPrice(double referPrice) {
            repository.findAll().stream().filter(product -> product.getPrice() > referPrice).
                    forEach(product -> System.out.println(product));
        }

        public Map<String, ProductType> sortByTitleAndDistinct() {
            return repository.findAll().stream().sorted(Comparator.comparing(Product::getTitle)).
                    distinct().collect(Collectors.toMap(Product::getId, Product::getType, (v1, v2) -> v2));
        }

        public DoubleSummaryStatistics getProductsPriceSummaryStatistic() {
            return repository.findAll().stream().mapToDouble(Product::getPrice).summaryStatistics();
        }

        public Product mapToProduct(Map<String, Object> productMap) {
            Function<Map<String, Object>, Product> mapToProduct = (map) -> {

                Object ptype = map.get("producttype");
                if (!(ptype instanceof ProductType)) {
                    throw new IllegalArgumentException();
                }
                ProductType type = (ProductType) ptype;
                return switch (type) {
                    case PHONE -> new Phone(map.getOrDefault("title", "N/A").toString(),
                            (Integer) map.getOrDefault("count", 0),
                            (Double) map.getOrDefault("price", 0D),
                            map.getOrDefault("model", "N/A").toString(),
                            Manufacturer.valueOf(map.getOrDefault("manufacturer", Manufacturer.UNKNOWN).toString()));

                    case TEA -> new Tea(map.getOrDefault("title", "N/A").toString(),
                            (Integer) map.getOrDefault("count", 0),
                            (Double) map.getOrDefault("price", 0L),
                            TeaBrand.valueOf(map.getOrDefault("brand", TeaBrand.UNKNOWN).toString()),
                            TeaType.valueOf(map.getOrDefault("type", TeaType.UNKNOWN).toString()));

                    case PEN -> new Pen(map.getOrDefault("title", "N/A").toString(),
                            (Integer) map.getOrDefault("count", 0),
                            (Double) map.getOrDefault("price", 0L),
                            PenBrand.valueOf(map.getOrDefault("brand", PenBrand.UNKNOWN).toString()),
                            PenType.valueOf(map.getOrDefault("type", PenType.UNKNOWN).toString()),
                            PenColor.valueOf(map.getOrDefault("color", PenColor.UNKNOWN).toString()));
                };

            };
            return mapToProduct.apply(productMap);
        }
    }
}

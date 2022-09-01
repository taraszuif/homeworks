package me.zuif.hw2.service;


import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.OperationSystem;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.repository.ProductRepository;
import me.zuif.hw2.repository.mongo.PhoneRepositoryMongo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@Singleton
public class PhoneService extends ProductService<Phone> {
    private static final Random RANDOM = new Random();
    private static PhoneService instance;

    @Autowired
    private PhoneService(ProductRepository repository) {
        super(repository);
    }

    public static PhoneService getInstance() {
        if (instance == null) {
            instance = new PhoneService(PhoneRepositoryMongo.getInstance());
        }
        return instance;
    }

    private Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    @Override
    protected Phone createProduct() {
        Phone phone = new Phone(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble(1000.0),
                "Model-" + RANDOM.nextInt(10),
                getRandomManufacturer()
        );
        return phone;
    }

    public Phone phoneFromMap(Map<String, String> productMap) {
        Function<Map<String, String>, Phone> mapToProduct = (map) -> {
            return new Phone(map.getOrDefault("title", "N/A"),
                    Integer.parseInt(map.getOrDefault("count", String.valueOf(0))),
                    Double.parseDouble(map.getOrDefault("price", String.valueOf(0))),
                    map.getOrDefault("model", "N/A"),
                    Manufacturer.valueOf(map.getOrDefault("manufacturer", Manufacturer.UNKNOWN.name())),
                    LocalDateTime.parse(map.get("created"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")),
                    map.get("currency"),
                    new OperationSystem(map.get("operating-system.designation"), Integer.parseInt(map.get("operating-system.version"))));

        };
        return mapToProduct.apply(productMap);
    }


}

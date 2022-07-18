package me.zuif.hw2.service;


import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.repository.phone.PhoneRepository;

import java.util.Random;

public class PhoneService extends ProductService<Phone> {
    private static final Random RANDOM = new Random();

    public PhoneService(PhoneRepository repository) {
        super(repository);
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


}

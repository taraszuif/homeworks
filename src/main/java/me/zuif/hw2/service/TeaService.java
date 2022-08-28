package me.zuif.hw2.service;


import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import me.zuif.hw2.repository.ProductRepository;
import me.zuif.hw2.repository.hibernate.TeaRepositoryHibernate;
import me.zuif.hw2.repository.postgres.TeaRepositoryDB;

import java.util.Random;

@Singleton
public class TeaService extends ProductService<Tea> {
    private static final Random RANDOM = new Random();
    private static TeaService instance;

    @Autowired
    private TeaService(ProductRepository repository) {
        super(repository);
    }

    public static TeaService getInstance() {
        if (instance == null) {
            instance = new TeaService(TeaRepositoryHibernate.getInstance());
        }
        return instance;
    }

    @Override
    protected Tea createProduct() {
        Tea tea = new Tea(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble(1000.0),
                getRandomTeaBrand(), getRandomTeaType()
        );
        return tea;
    }

    private TeaBrand getRandomTeaBrand() {
        final TeaBrand[] values = TeaBrand.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    private TeaType getRandomTeaType() {
        final TeaType[] values = TeaType.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }


}

package me.zuif.hw2.service;


import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.ProductRepository;
import me.zuif.hw2.repository.hibernate.PenRepositoryHibernate;
import me.zuif.hw2.repository.postgres.PenRepositoryDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Singleton
public class PenService extends ProductService<Pen> {
    private static final Random RANDOM = new Random();
    private static PenService instance;

    @Autowired
    private PenService(ProductRepository repository) {
        super(repository);
    }

    public static PenService getInstance() {
        if (instance == null) {
            instance = new PenService(PenRepositoryHibernate.getInstance());
        }
        return instance;
    }

    private PenBrand getRandomPenBrand() {
        final PenBrand[] values = PenBrand.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    private PenType getRandomPenType() {
        final PenType[] values = PenType.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    private PenColor getRandomPenColor() {
        final PenColor[] values = PenColor.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }


    @Override
    public Pen createProduct() {
        return new Pen(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble(1000.0),
                getRandomPenBrand(), getRandomPenType(), getRandomPenColor()
        );
    }

    //ifPresentOrElse
    public void updateOrSave(Pen pen) {

        getRepository().findById(pen.getId()).ifPresentOrElse(pen1 -> update(pen1), () -> save(pen));
    }

    //orElseThrow
    public Pen findByIdOrThrow(String id) {
        return getRepository().findById(id).orElseThrow(IllegalArgumentException::new);
    }

    //orElseGet
    public Pen findByIdOrGetAny(String id) {
        return getRepository().findById(id).orElseGet(() -> getRepository().findAll().stream().findAny().get());
    }

    //orElse
    public Pen findByIdOrGetRandom(String id) {
        return getRepository().findById(id).orElse(createProduct());
    }

    //or
    public Optional<Pen> findByIdOrGetFirst(String id) {
        return getRepository().findById(id).or(() -> getRepository().findAll().stream().findFirst());
    }

    //ifPresent
    public void deleteIfPresent(String id) {
        getRepository().findById(id).ifPresent(pen -> {
            getRepository().delete(pen.getId());
        });
    }

    //filter
    public List<Pen> getAllByBrand(PenBrand brand) {
        List<Pen> filtered = new ArrayList<>();
        for (Pen pen : getRepository().findAll()) {
            Optional.of(pen).filter(penf -> penf.getBrand() == brand).ifPresent(pen1 -> filtered.add(pen1));
        }
        return filtered;
    }

    //map
    public String mapToString(String id) {
        return getRepository().findById(id).map(Pen::toString).get();
    }


}

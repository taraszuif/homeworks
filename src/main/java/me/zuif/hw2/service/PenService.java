package me.zuif.hw2.service;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.pen.PenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PenService {
    private static final Random RANDOM = new Random();

    private static final Logger logger = LoggerFactory.getLogger(PenService.class);
    private final PenRepository repository;

    public PenService(PenRepository repository) {
        this.repository = repository;
    }

    public void createAndSavePens(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        List<Pen> pens = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final Pen pen = getRandomPen();
            pens.add(pen);
            logger.info("Pen {} has been saved", pen.getId());
        }
        repository.saveAll(pens);
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

    public void update(Pen pen) {
        repository.update(pen);
    }

    public Pen getRandomPen() {
        return new Pen(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble(1000.0),
                getRandomPenBrand(), getRandomPenType(), getRandomPenColor()
        );
    }

    //ifPresentOrElse
    public void updateOrSave(Pen pen) {
        repository.findById(pen.getId()).ifPresentOrElse(pen1 -> update(pen1), () -> savePen(pen));
    }

    //orElseThrow
    public Pen findByIdOrThrow(String id) {
        return repository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    //orElseGet
    public Pen findByIdOrGetAny(String id) {
        return repository.findById(id).orElseGet(() -> repository.getAll().stream().findAny().get());
    }

    //orElse
    public Pen findByIdOrGetRandom(String id) {
        return repository.findById(id).orElse(getRandomPen());
    }

    //or
    public Optional<Pen> findByIdOrGetFirst(String id) {
        return repository.findById(id).or(() -> repository.getAll().stream().findFirst());
    }


    public List<Pen> getAll() {
        return repository.getAll();
    }

    public void delete(String id) {
        repository.delete(id);
    }

    //ifPresent
    public void deleteIfPresent(String id) {
        repository.findById(id).ifPresent(pen -> {
            repository.delete(pen.getId());
        });
    }

    //filter
    public List<Pen> getAllByBrand(PenBrand brand) {
        List<Pen> filtered = new ArrayList<>();
        for (Pen pen : repository.getAll()) {
            Optional.of(pen).filter(penf -> penf.getBrand() == brand).ifPresent(pen1 -> filtered.add(pen1));
        }
        return filtered;
    }

    public void savePen(Pen pen) {
        if (pen.getCount() == 0) {
            pen.setCount(-1);
        }
        repository.save(pen);
    }

    public void printAll() {
        for (Pen pen : repository.getAll()) {
            System.out.println(pen);
        }
    }

    //map
    public String mapToString(String id) {
        return repository.findById(id).map(Pen::toString).get();
    }


}

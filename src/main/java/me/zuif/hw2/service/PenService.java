package me.zuif.hw2.service;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.pen.PenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
            final Pen pen = new Pen(
                    "Title-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(500),
                    RANDOM.nextDouble(1000.0),
                    getRandomPenBrand(), getRandomPenType(), getRandomPenColor()
            );
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

    public Optional<Pen> findById(String id) {
        return repository.findById(id);
    }

    public List<Pen> getAll() {
        return repository.getAll();
    }

    public void delete(String id) {
        repository.delete(id);
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


}

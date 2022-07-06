package me.zuif.hw2.service.pen;


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
    private static final PenRepository REPOSITORY = new PenRepository();
    private static final Logger logger = LoggerFactory.getLogger(PenService.class);

    public void createAndSavePens(int count) {
        List<Pen> pens = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            Pen pen = new Pen(
                    "Title-" + RANDOM.nextInt(10000),
                    RANDOM.nextInt(250000),
                    RANDOM.nextDouble(30.0),
                    getRandomPenBrand(),
                    getRandomPenType(),
                    getRandomPenColor()
            );
            logger.info("Creating " + pen);
            pens.add(pen);
        }
        REPOSITORY.saveAll(pens);
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
        REPOSITORY.update(pen);
    }

    public Optional<Pen> findById(String id) {
        return REPOSITORY.findById(id);
    }

    public List<Pen> getAll() {
        return REPOSITORY.getAll();
    }

    public void delete(String id) {
        REPOSITORY.delete(id);
    }

    public void printAll() {
        for (Pen pen : REPOSITORY.getAll()) {
            System.out.println(pen);
        }
    }


}

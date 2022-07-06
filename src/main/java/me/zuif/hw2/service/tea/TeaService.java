package me.zuif.hw2.service.tea;


import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import me.zuif.hw2.repository.tea.TeaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TeaService {
    private static final Random RANDOM = new Random();
    private static final TeaRepository REPOSITORY = new TeaRepository();
    private static final Logger logger = LoggerFactory.getLogger(TeaService.class);

    public void createAndSaveTeas(int count) {
        List<Tea> teas = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            Tea tea = new Tea(
                    "Title-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(500000),
                    RANDOM.nextDouble(10.0),
                    getRandomTeaBrand(),
                    getRandomTeaType()
            );
            logger.info("Creating " + tea);
            teas.add(tea);
        }
        REPOSITORY.saveAll(teas);
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

    public void update(Tea phone) {
        REPOSITORY.update(phone);
    }

    public Optional<Tea> findById(String id) {
        return REPOSITORY.findById(id);
    }

    public List<Tea> getAll() {
        return REPOSITORY.getAll();
    }

    public void delete(String id) {
        REPOSITORY.delete(id);
    }

    public void printAll() {
        for (Tea tea : REPOSITORY.getAll()) {
            System.out.println(tea);
        }
    }
}

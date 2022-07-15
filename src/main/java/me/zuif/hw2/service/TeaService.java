package me.zuif.hw2.service;


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
    private static final Logger logger = LoggerFactory.getLogger(TeaService.class);
    private final TeaRepository repository;

    public TeaService(TeaRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveTeas(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        List<Tea> teas = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final Tea tea = new Tea(
                    "Title-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(500),
                    RANDOM.nextDouble(1000.0),
                    getRandomTeaBrand(), getRandomTeaType()
            );
            teas.add(tea);
            logger.info("Tea {} has been saved", tea.getId());
        }
        repository.saveAll(teas);
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

    public void update(Tea tea) {
        repository.update(tea);
    }

    public void saveTea(Tea tea) {
        if (tea.getCount() == 0) {
            tea.setCount(-1);
        }
        repository.save(tea);
    }

    public Optional<Tea> findById(String id) {
        return repository.findById(id);
    }

    public List<Tea> getAll() {
        return repository.getAll();
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void printAll() {
        for (Tea tea : repository.getAll()) {
            System.out.println(tea);
        }
    }
}

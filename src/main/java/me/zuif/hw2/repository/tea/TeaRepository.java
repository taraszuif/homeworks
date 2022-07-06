package me.zuif.hw2.repository.tea;

import me.zuif.hw2.model.tea.Tea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class TeaRepository implements CrudTeaRepository {
    private final Logger logger = LoggerFactory.getLogger(TeaRepository.class);
    private final List<Tea> teas;

    public TeaRepository() {
        teas = new LinkedList<>();
    }

    @Override
    public void save(Tea tea) {
        teas.add(tea);
    }

    @Override
    public void saveAll(List<Tea> teas) {
        for (Tea tea : teas) {
            save(tea);
        }
    }

    @Override
    public boolean update(Tea tea) {
        final Optional<Tea> result = findById(tea.getId());
        if (result.isEmpty()) {
            return false;
        }
        final Tea originTea = result.get();
        TeaCopy.copy(tea, originTea);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Iterator<Tea> iterator = teas.iterator();
        while (iterator.hasNext()) {
            final Tea tea = iterator.next();
            if (tea.getId().equals(id)) {
                logger.info("Deleting {}", tea);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Tea> getAll() {
        if (teas.isEmpty()) {
            return Collections.emptyList();
        }
        return teas;
    }

    @Override
    public Optional<Tea> findById(String id) {
        Tea result = null;
        for (Tea tea : teas) {
            if (tea.getId().equals(id)) {
                result = tea;
            }
        }
        return Optional.ofNullable(result);
    }

    private static class TeaCopy {
        private static void copy(final Tea from, final Tea to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }
}

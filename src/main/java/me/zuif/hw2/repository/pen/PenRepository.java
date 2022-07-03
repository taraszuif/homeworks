package me.zuif.hw2.repository.pen;


import me.zuif.hw2.model.pen.Pen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PenRepository implements CrudPenRepository {
    private final List<Pen> pens;
    private final Logger logger = LoggerFactory.getLogger(PenRepository.class);

    public PenRepository() {
        pens = new LinkedList<>();
    }

    @Override
    public void save(Pen pen) {
        pens.add(pen);
    }

    @Override
    public void saveAll(List<Pen> pens) {
        for (Pen pen : pens) {
            save(pen);
        }
    }

    @Override
    public boolean update(Pen pen) {
        final Optional<Pen> result = findById(pen.getId());
        if (result.isEmpty()) {
            return false;
        }
        final Pen originPen = result.get();
        PenCopy.copy(pen, originPen);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Iterator<Pen> iterator = pens.iterator();
        while (iterator.hasNext()) {
            final Pen pen = iterator.next();
            if (pen.getId().equals(id)) {
                logger.info("Deleting " + pen);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Pen> getAll() {
        if (pens.isEmpty()) {
            return Collections.emptyList();
        }
        return pens;
    }

    @Override
    public Optional<Pen> findById(String id) {
        Pen result = null;
        for (Pen pen : pens) {
            if (pen.getId().equals(id)) {
                result = pen;
            }
        }
        return Optional.ofNullable(result);
    }

    private static class PenCopy {
        private static void copy(final Pen from, final Pen to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }
}

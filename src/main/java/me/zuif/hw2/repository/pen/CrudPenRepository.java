package me.zuif.hw2.repository.pen;


import me.zuif.hw2.model.pen.Pen;

import java.util.List;
import java.util.Optional;

public interface CrudPenRepository {
    void save(Pen pen);

    void saveAll(List<Pen> pens);

    boolean update(Pen pen);

    boolean delete(String id);

    List<Pen> getAll();

    Optional<Pen> findById(String id);
}

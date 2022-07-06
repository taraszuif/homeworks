package me.zuif.hw2.repository.tea;


import me.zuif.hw2.model.tea.Tea;

import java.util.List;
import java.util.Optional;

public interface CrudTeaRepository {
    void save(Tea phone);

    void saveAll(List<Tea> phones);

    boolean update(Tea phone);

    boolean delete(String id);

    List<Tea> getAll();

    Optional<Tea> findById(String id);
}

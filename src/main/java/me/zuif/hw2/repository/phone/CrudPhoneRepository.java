package me.zuif.hw2.repository.phone;


import me.zuif.hw2.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface CrudPhoneRepository  {
    void save(Phone phone);

    void saveAll(List<Phone> phones);

    boolean update(Phone phone);

    boolean delete(String id);

    List<Phone> getAll();

    Optional<Phone> findById(String id);
}

package me.zuif.hw2.repository.phone;


import me.zuif.hw2.model.phone.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PhoneRepository implements CrudPhoneRepository {
    private final List<Phone> phones;
    private final Logger logger = LoggerFactory.getLogger(PhoneRepository.class);

    public PhoneRepository() {
        phones = new LinkedList<>();
    }

    @Override
    public void save(Phone phone) {
        if (phone == null) {
            final IllegalArgumentException exception = new IllegalArgumentException("Cannot save a null phone");
            logger.error(exception.getMessage(), exception);
            throw exception;
        } else {
            checkDuplicates(phone);
            phones.add(phone);
        }
    }

    private void checkDuplicates(Phone phone) {
        for (Phone p : phones) {
            if (phone.hashCode() == p.hashCode() && phone.equals(p)) {
                final IllegalArgumentException exception = new IllegalArgumentException("Duplicate phone: " +
                        phone.getId());
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }
    }

    @Override
    public void saveAll(List<Phone> phones) {
        for (Phone phone : phones) {
            save(phone);
        }
    }

    @Override
    public boolean update(Phone phone) {
        final Optional<Phone> result = findById(phone.getId());
        if (result.isEmpty()) {
            return false;
        }
        final Phone originPhone = result.get();
        PhoneCopy.copy(phone, originPhone);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Iterator<Phone> iterator = phones.iterator();
        while (iterator.hasNext()) {
            final Phone phone = iterator.next();
            if (phone.getId().equals(id)) {
                logger.info("Deleting {}", phone);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Phone> findAll() {
        if (phones.isEmpty()) {
            return Collections.emptyList();
        }
        return phones;
    }

    @Override
    public Optional<Phone> findById(String id) {
        Phone result = null;
        for (Phone phone : phones) {
            if (phone.getId().equals(id)) {
                result = phone;
            }
        }
        return Optional.ofNullable(result);
    }

    private static class PhoneCopy {
        private static void copy(final Phone from, final Phone to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }
}

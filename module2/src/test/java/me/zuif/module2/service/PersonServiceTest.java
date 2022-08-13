package me.zuif.module2.service;

import me.zuif.module2.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonServiceTest {
    private PersonService target;

    @BeforeEach
    void setUp() {
        target = PersonService.getInstance();
    }

    @Test
    void generateRandomCustomer() {
        Customer customer = target.generateRandomCustomer();

        Assertions.assertNotNull(customer);
    }
}
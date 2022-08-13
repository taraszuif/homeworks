package me.zuif.module2.service;

import me.zuif.module2.model.Customer;

import java.util.Random;

public class PersonService {
    private static PersonService instance;
    private final Random RANDOM;

    private PersonService() {
        RANDOM = new Random();
    }

    public static PersonService getInstance() {
        if (instance == null) {
            instance = new PersonService();
        }
        return instance;
    }

    public Customer generateRandomCustomer() {
        int age = RANDOM.nextInt(10, 100);
        String email = getSaltString() + "@gmail.com";
        Customer customer = new Customer(email, age);
        return customer;
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SALTCHARS = SALTCHARS + SALTCHARS.toLowerCase();
        SALTCHARS += "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}

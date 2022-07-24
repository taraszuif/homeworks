package me.zuif.hw2;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.repository.PenRepository;
import me.zuif.hw2.repository.PhoneRepository;
import me.zuif.hw2.repository.TeaRepository;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.TeaService;
import me.zuif.hw2.util.ProductComparator;
import me.zuif.hw2.util.ProductLinkedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    private static final PhoneService PHONE_SERVICE = new PhoneService(new PhoneRepository());
    private static final TeaService TEA_SERVICE = new TeaService(new TeaRepository());
    private static final PenService PEN_SERVICE = new PenService(new PenRepository());

    public static void main(String[] args) {
        //comparator test
        System.out.println("Comparator test:");
        PHONE_SERVICE.createAndSaveProducts(10);
        List<Phone> list = new ArrayList<>();
        list.addAll(PHONE_SERVICE.findAll());
        list.sort(new ProductComparator<>());
        list.forEach(System.out::println);

        System.out.println("-".repeat(20));
        //ProductLinkedList test
        ProductLinkedList<Pen> productLinkedList = new ProductLinkedList<>();

        System.out.println("ProductLinkedList test");

        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 12);
        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 281);
        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 31);
        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 165);
        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 61);
        productLinkedList.addFirst(PEN_SERVICE.createProduct(), 713);
        System.out.println("ForEach: ");
        productLinkedList.forEach(System.out::println);

        System.out.println("Find by version 281: \n" + productLinkedList.findByVersion(281) + "\n");

        Pen testpen = PEN_SERVICE.createProduct();
        testpen.setCount(1400);
        System.out.println("Set by version 31 from " + productLinkedList.findByVersion(31) + " to: " + testpen);

        productLinkedList.setByVersion(31, testpen);
        System.out.println("ForEach: ");
        productLinkedList.forEach(System.out::println);

        System.out.println("Deleting by version 31");
        productLinkedList.deleteByVersion(31);
        System.out.println("ForEach: ");
        productLinkedList.forEach(System.out::println);

        System.out.println("Versions count: " + productLinkedList.getVersionCount());

        System.out.println("FirstDateVersion: " + productLinkedList.getFirstVersionDate());
        System.out.println("LastDateVersion: " + productLinkedList.getLastVersionDate());

        System.out.println("Iterator: ");
        Iterator<Pen> penIterator = productLinkedList.iterator();
        while (penIterator.hasNext()) {
            System.out.println(penIterator.next());
        }

        System.out.println("-".repeat(20));

        //PEN
        PEN_SERVICE.createAndSaveProducts(5);
        System.out.println("Pens created:");
        PEN_SERVICE.printAll();
        Pen pen = PEN_SERVICE.findAll().get(4);
        System.out.println("Changing count of pen with index 4 to 25 ");
        System.out.println("Before: " + pen);
        pen.setCount(25);
        PEN_SERVICE.update(pen);
        System.out.println("After: " + pen);
        System.out.println("Pen list:");
        PEN_SERVICE.printAll();
        System.out.println("Deleting " + pen);
        PEN_SERVICE.delete(pen.getId());
        System.out.println("Pen list:");
        PEN_SERVICE.printAll();
    }
}

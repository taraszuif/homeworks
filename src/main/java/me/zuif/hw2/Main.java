package me.zuif.hw2;


import me.zuif.hw2.command.*;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.ProductService;
import me.zuif.hw2.service.TeaService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final TeaService TEA_SERVICE = TeaService.getInstance();
    private static final PenService PEN_SERVICE = PenService.getInstance();

    public static void main(String[] args) {
        streamTest();
        final Commands[] values = Commands.values();
        final List<String> names = Utils.getNamesOfEnum(values);

        boolean exit = false;
        do {
            int commandIndex = UserInputUtil.getUserInput(values.length, names);
            switch (values[commandIndex]) {
                case DELETE -> new Delete().execute();
                case PRINT -> new Print().execute();
                case CREATE -> new Create().execute();
                case UPDATE -> new Update().execute();
                case EXIT -> exit = true;
            }

        } while (!exit);

    }

    private static void streamTest() {
        PHONE_SERVICE.createAndSaveProducts(10);
        System.out.println("ForEach: ");
        PHONE_SERVICE.printAll();
        ProductService.StreamHomework homework = PHONE_SERVICE.new StreamHomework<Pen>();

        //Посчитать сумму товаров через reduce
        System.out.println("product sum count: " + homework.sumProduct());
        System.out.println();

        //Отсортировать товары по названию, убрать дубликаты, преобразовать в Map где ключ это id товара, а значение это его тип
        System.out.println("sort by title and distinct " + homework.sortByTitleAndDistinct());

        System.out.println();

        //Получить статистику по цене всех товаров
        System.out.println("Summary statistic " + homework.getProductsPriceSummaryStatistic());
        System.out.println();

        List<String> details = Arrays.asList("first", "second");
        Phone phone = new Phone("test title", 99, 501, "testmodel", Manufacturer.APPLE, details);
        PHONE_SERVICE.save(phone);
        //Добавить в один товар коллекцию деталей (например List<String> details), проверить среди всех товаров есть ли наличие конкретной детали
        System.out.println("Check has detail first: " + PHONE_SERVICE.checkDetailExists("first"));
        System.out.println();

        //Написать реализацию предиката который проверяет что в переданной коллекции у всех предметов есть цена.
        System.out.println("Check if phone collection has price: " + homework.hasPrice.test(PHONE_SERVICE.findAll()));
        Phone noPrice = new Phone("test title", 99, 0, "testmodel", Manufacturer.APPLE, details);
        PHONE_SERVICE.save(noPrice);
        System.out.println("Check if phone collection hasn't price: " + homework.hasPrice.test(PHONE_SERVICE.findAll()));


        Map<String, Object> map = new HashMap<>();
        map.put("producttype", phone.getType());
        map.put("title", phone.getTitle());
        map.put("count", phone.getCount());
        map.put("price", phone.getPrice());
        map.put("manufacturer", phone.getManufacturer());
        map.put("model", phone.getModel());
        //Написать реализацию Function которая принимает Map<String, Object> и создает конкретный продукт на основании полей Map
        System.out.println("map " + map + " to product: " + homework.mapToProduct(map));
        System.out.println();

        //Найти товары дороже цены Х и показать их наименование
        System.out.println("print greater refer price 500: ");
        homework.printGreaterThanReferPrice(500);

    }

}


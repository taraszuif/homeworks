package me.zuif.hw2;


import me.zuif.hw2.command.*;
import me.zuif.hw2.context.ApplicationContext;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.ProductService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;
import me.zuif.hw2.util.parser.JsonParser;
import me.zuif.hw2.util.parser.XmlParser;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();

    public static void main(String[] args) {
        applicationContextTest();
        /*  builderTest();*/
        /* parserTest();*/
        /*streamTest();*/
        /*commandsTest();*/
    }


    private static void applicationContextTest() {
        ApplicationContext context = ApplicationContext.getInstance();
        context.setCache();
        System.out.println(context.getCache());


    }

    private static void builderTest() {
        Phone.Builder builder = new Phone.Builder(10, Manufacturer.APPLE);
        builder.withCount(10);
        builder.withTitle("Typical Title");
        builder.withModel("Model-1");
        Phone phone = builder.build();
        System.out.println(phone);
    }

    private static void parserTest() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStreamJSON = loader.getResourceAsStream("phone.json");
        InputStream inputStreamXML = loader.getResourceAsStream("phone.xml");
        System.out.println("JSON: " + PHONE_SERVICE.phoneFromMap
                (JsonParser.jsonLinesToMap(
                        JsonParser.jsonToLines(inputStreamJSON))));
        System.out.println();

        System.out.println("XML: " + PHONE_SERVICE.phoneFromMap(
                XmlParser.xmlLinesToMap(
                        XmlParser.xmlToLines(inputStreamXML))));
    }

    private static void commandsTest() {
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
        ProductService.StreamHomework homework = PHONE_SERVICE.new StreamHomework<Phone>();

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
        map.put("example", phone.getModel());
        //Написать реализацию Function которая принимает Map<String, Object> и создает конкретный продукт на основании полей Map
        System.out.println("map " + map + " to product: " + homework.mapToProduct(map));
        System.out.println();

        //Найти товары дороже цены Х и показать их наименование
        System.out.println("print greater refer price 500: ");
        homework.printGreaterThanReferPrice(500);

    }

}


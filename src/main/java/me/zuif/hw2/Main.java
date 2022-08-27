package me.zuif.hw2;


import me.zuif.hw2.command.*;
import me.zuif.hw2.context.ApplicationContext;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.service.InvoiceService;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        hibernateTest();
        /*dbTest();*/
        /*  applicationContextTest();*/
        /*  builderTest();*/
        /* parserTest();*/
        /*streamTest();*/
        /*commandsTest();*/
    }

    private static void hibernateTest() {
        PhoneService phoneService = PhoneService.getInstance();
        phoneService.createAndSaveProducts(10);
        PenService penService = PenService.getInstance();
        penService.createAndSaveProducts(10);
        InvoiceService service = InvoiceService.getInstance();
        List<Product> products = new ArrayList<>();
        products.addAll(phoneService.findAll());
        service.createFromProducts(products);
        System.out.println(service.getInvoiceCount());
        System.out.println(service.findAllGreaterSumInvoices(100));
        System.out.println(service.sortBySum());
    }
    private static void dbTest() {
        PhoneService phoneService = PhoneService.getInstance();
        phoneService.createAndSaveProducts(10);
        PenService penService = PenService.getInstance();
        penService.createAndSaveProducts(10);
        InvoiceService service = InvoiceService.getInstance();
        List<Product> products = new ArrayList<>();
        products.addAll(phoneService.findAll());
        service.createFromProducts(products);
        System.out.println(service.getInvoiceCount());
        System.out.println(service.findAllGreaterSumInvoices(100));
        System.out.println(service.sortBySum());
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


}


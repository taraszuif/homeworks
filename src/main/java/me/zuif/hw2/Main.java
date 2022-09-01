package me.zuif.hw2;


import me.zuif.hw2.command.*;
import me.zuif.hw2.config.FlywayConfig;
import me.zuif.hw2.config.HibernateSessionFactoryUtil;
import me.zuif.hw2.context.ApplicationContext;
import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.service.InvoiceService;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.TeaService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;
import org.flywaydb.core.Flyway;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Flyway flyway = FlywayConfig.getInstance();
        flyway.clean();
        HibernateSessionFactoryUtil.getSessionFactory();
        flyway.migrate();
        /* mongoTest();*/
        hibernateTest();
        /*dbTest();*/
        /*  applicationContextTest();*/
        /*  builderTest();*/
        /* parserTest();*/
        /*streamTest();*/
        commandsTest();
    }

    private static void mongoTest() {
        TeaService teaService = TeaService.getInstance();
        teaService.createAndSaveProducts(10);
        PenService penService = PenService.getInstance();
        penService.createAndSaveProducts(10);
        PhoneService phoneService = PhoneService.getInstance();
        phoneService.createAndSaveProducts(10);
        Tea tea = teaService.findAll().stream().findAny().get();
        tea.setTitle("Test");
        teaService.update(tea);
        System.out.println(teaService.findAll());
        teaService.delete(tea.getId());
        System.out.println(teaService.findAll());
        InvoiceService service = InvoiceService.getInstance();
        List<Product> products = new ArrayList<>();
        products.addAll(teaService.findAll());
        Invoice invoice = service.createFromProducts(products);
        System.out.println("invoice count" + service.getInvoiceCount());
        System.out.println("findAllGreaterSumInvoices: " + service.findAllGreaterSumInvoices(100));
        System.out.println("sortBySum: " + service.sortBySum());
        System.out.println("find by id " + service.findById(invoice.getId()));
    }

    private static void hibernateTest() {
        TeaService teaService = TeaService.getInstance();
        teaService.createAndSaveProducts(10);
        PenService penService = PenService.getInstance();
        penService.createAndSaveProducts(10);
        PhoneService phoneService = PhoneService.getInstance();
        phoneService.createAndSaveProducts(10);
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


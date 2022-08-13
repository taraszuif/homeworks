package me.zuif.module2.controller;

import me.zuif.module2.model.Customer;
import me.zuif.module2.model.invoice.Invoice;
import me.zuif.module2.model.invoice.InvoiceType;
import me.zuif.module2.model.product.ProductType;
import me.zuif.module2.service.PersonService;
import me.zuif.module2.service.ShopService;
import me.zuif.module2.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Controller implements Runnable {
    private final ShopService SHOP_SERVICE;
    private final PersonService PERSON_SERVICE;
    private final Scanner SCANNER;

    public Controller() {
        SHOP_SERVICE = ShopService.getInstance();
        PERSON_SERVICE = PersonService.getInstance();
        SCANNER = new Scanner(System.in);
    }

    @Override
    public void run() {
        System.out.println("Start");
        System.out.println();
        System.out.println("Parsing products..");
        InputStream csvStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("table.csv");
        SHOP_SERVICE.createProducts(csvStream);
        System.out.println("Parsed: ");
        SHOP_SERVICE.getProducts().forEach(System.out::println);
        System.out.println();

        try {
            setPredicate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println("Creating 15 random invoices..");
        SHOP_SERVICE.getInvoices().addAll(generateInvoices(15));
        System.out.println("Created: ");
        SHOP_SERVICE.getInvoices().forEach(System.out::println);
        System.out.println();

        //Количество проданных товаров по категориям (Telephone/Television)
        System.out.println("telephones count sold: " + SHOP_SERVICE.getSoldProductsCountByType(ProductType.TELEPHONE));
        System.out.println();
        System.out.println("televisions count sold: " + SHOP_SERVICE.getSoldProductsCountByType(ProductType.TELEVISION));
        System.out.println();

        //Сумма самого маленького чека и информация о покупателе
        Map<Double, Customer> doubleCustomerMap = SHOP_SERVICE.getInvoiceSumAndCustomerInfo();
        System.out.println("Lowest invoice:");
        doubleCustomerMap.entrySet().stream().findFirst().
                ifPresent(entry -> System.out.println("sum: " + entry.getKey() + ", customer info: " + entry.getValue()));
        System.out.println();

        //Сумма всех покупок
        System.out.println("Invoices sum: " + SHOP_SERVICE.getInvoicesSum());
        System.out.println();

        //Количество чеков с категорией retail
        System.out.println("Invoices count with RETAIL category: " + SHOP_SERVICE.getInvoiceCountByType(InvoiceType.RETAIL));
        System.out.println();

        //Чеки которые содержат только один тип товара
        System.out.println("Invoices that has only one type of the product: ");
        System.out.println("Television:");
        SHOP_SERVICE.getInvoices().stream().
                filter(invoice -> SHOP_SERVICE.hasOnlyOneProductType(invoice.getProductList(), ProductType.TELEVISION)).
                forEach(System.out::println);
        System.out.println();
        System.out.println("Telephone:");
        SHOP_SERVICE.getInvoices().stream().
                filter(invoice -> SHOP_SERVICE.hasOnlyOneProductType(invoice.getProductList(), ProductType.TELEPHONE)).
                forEach(System.out::println);
        System.out.println();

        //Первые три чека сделанные покупателями
        System.out.println("First three invoices: " + SHOP_SERVICE.getFirstInvoices(3));
        System.out.println();

        //Информация по чекам купленных пользователем младше 18 лет, при этом изменить тип чека на low_age
        System.out.println("Invoices by customers under age 18: ");
        List<Invoice> invoicesUnderAge18 = SHOP_SERVICE.getInvoicesByCustomersUnderAge(18, true);
        if (invoicesUnderAge18.size() != 0) {
            invoicesUnderAge18.forEach(System.out::println);
        } else {
            System.out.println("Not found");
        }
        System.out.println();

        //Метод который сортирует все заказы в следующем порядке:
        //Сначала по возрасту покупателя от большего к меньшему
        //Далее по количеству купленных предметов
        //Далее по общей сумме купленных предметов
        System.out.println("Sorted invoices: ");
        SHOP_SERVICE.getSortedInvoices().forEach(System.out::println);
        System.out.println();

        System.out.println("End");
    }

    public List<Invoice> generateInvoices(int count) {
        List<Invoice> invoices = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            invoices.add(SHOP_SERVICE.generateRandomInvoice(PERSON_SERVICE.generateRandomCustomer()));
        });
        return invoices;
    }

    public void setPredicate() throws IOException {
        String line;
        boolean numeric;
        do {
            System.out.println("Enter the invoice limit that uses to set the invoice type. Default: 1000");
            line = SCANNER.nextLine();
            numeric = Utils.isNumeric(line);
            if (numeric) {
                double limit = Double.parseDouble(line);
                SHOP_SERVICE.setPredicate(sum -> sum > limit);
                System.out.println("Successfully set limit to " + limit);
                return;
            }
            System.out.println("Invalid input: limit not number. Try again");
        } while (numeric);

    }
}

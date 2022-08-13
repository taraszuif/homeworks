package me.zuif.module2.service;

import lombok.SneakyThrows;
import me.zuif.module2.model.Customer;
import me.zuif.module2.model.invoice.Invoice;
import me.zuif.module2.model.invoice.InvoiceType;
import me.zuif.module2.model.product.Product;
import me.zuif.module2.model.product.ProductType;
import me.zuif.module2.model.product.ScreenType;
import me.zuif.module2.model.product.Telephone;
import me.zuif.module2.model.product.television.Country;
import me.zuif.module2.model.product.television.Television;
import me.zuif.module2.util.CsvParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class ShopServiceTest {
    private ShopService target;

    @BeforeEach
    void setUp() {
        target = ShopService.getInstance();
    }

    @SneakyThrows
    private void setUpProducts() {
        target.setProducts(List.of());
        target.setInvoices(List.of());
        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        target.setProducts(List.of(telephone, television));
    }


    @Test
    void createProducts() {
        try (MockedStatic<CsvParser> mockedStatic = Mockito.mockStatic(CsvParser.class)) {
            CsvParser parser = Mockito.mock(CsvParser.class);
            mockedStatic.when(() -> CsvParser.getInstance()).thenReturn(parser);


            Map<String, String> telephoneMap = new HashMap<>();
            telephoneMap.put("type", "Telephone");
            telephoneMap.put("price", String.valueOf(10));
            telephoneMap.put("series", "1-S");
            telephoneMap.put("model", "Samsung");
            telephoneMap.put("screen type", "LED");

            Map<String, String> televisionMap = new HashMap<>();
            televisionMap.put("type", "Television");
            televisionMap.put("price", String.valueOf(20));
            televisionMap.put("country", "China");
            televisionMap.put("series", "2-S");
            televisionMap.put("screen type", "IPS");
            televisionMap.put("diagonal", "30");

            Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
            Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

            Mockito.when(parser.getCsvData((InputStream) Mockito.any())).thenReturn(List.of(telephoneMap, televisionMap));

            InputStream stream = Mockito.mock(InputStream.class);
            List<Product> products = target.createProducts(stream);

            Assertions.assertEquals(products.get(0), telephone);
            Assertions.assertEquals(products.get(1), television);
        }

    }

    @Test
    void createProducts_negative() {
        try (MockedStatic<CsvParser> mockedStatic = Mockito.mockStatic(CsvParser.class)) {
            CsvParser parser = Mockito.mock(CsvParser.class);
            mockedStatic.when(() -> CsvParser.getInstance()).thenReturn(parser);


            Map<String, String> telephoneMap = new HashMap<>();
            telephoneMap.put("type", "Telephone");
            telephoneMap.put("price", String.valueOf(10));
            telephoneMap.put("series", "1-S");
            telephoneMap.put("model", "Samsung");
            telephoneMap.put("screen type", "QLED");

            Map<String, String> televisionMap = new HashMap<>();
            televisionMap.put("type", "Television");
            televisionMap.put("price", String.valueOf(20));
            televisionMap.put("country", "China");
            televisionMap.put("series", "12321-S");
            televisionMap.put("screen type", "IPS");
            televisionMap.put("diagonal", "30");

            Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
            Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

            Mockito.when(parser.getCsvData((InputStream) Mockito.any())).thenReturn(List.of(telephoneMap, televisionMap));

            InputStream stream = Mockito.mock(InputStream.class);
            List<Product> products = target.createProducts(stream);

            Assertions.assertNotEquals(products.get(0), telephone);
            Assertions.assertNotEquals(products.get(1), television);
        }

    }

    @Test
    void getInvoiceSum() {
        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        double actual = target.getInvoiceSum(List.of(telephone, television));
        double expected = 30;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoiceSum_negative() {
        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        double actual = target.getInvoiceSum(List.of(telephone, television));
        double unexpected = 31;

        Assertions.assertNotEquals(unexpected, actual);
    }


    @Test
    void generateRandomInvoice() {
        setUpProducts();

        Customer customer = PersonService.getInstance().generateRandomCustomer();
        Invoice invoice = target.generateRandomInvoice(customer);

        Assertions.assertTrue(!invoice.getProductList().isEmpty());
        Assertions.assertSame(customer, invoice.getCustomer());
    }


    @Test
    void generateRandomInvoice_negative() {
        setUpProducts();

        Customer customer = PersonService.getInstance().generateRandomCustomer();
        Customer otherCustomer = PersonService.getInstance().generateRandomCustomer();
        Invoice invoice = target.generateRandomInvoice(customer);

        Assertions.assertFalse(invoice.getProductList().isEmpty());
        Assertions.assertNotSame(otherCustomer, invoice.getCustomer());
    }


    @SneakyThrows
    @Test
    void getInvoiceCountByType() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        Customer customer3 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setType(InvoiceType.WHOLESALE);

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setType(InvoiceType.RETAIL);

        Invoice invoice3 = target.generateRandomInvoice(customer3);
        invoice3.setType(InvoiceType.RETAIL);

        Field field = target.getClass().getDeclaredField("invoices");
        field.setAccessible(true);
        field.set(target, List.of(invoice1, invoice2, invoice3));

        long actualRetailCount = target.getInvoiceCountByType(InvoiceType.RETAIL);
        long expectedRetailCount = 2;

        long actualWholesaleCount = target.getInvoiceCountByType(InvoiceType.WHOLESALE);
        long expectedWholesaleCount = 1;

        Assertions.assertEquals(expectedRetailCount, actualRetailCount);
        Assertions.assertEquals(expectedWholesaleCount, actualWholesaleCount);
    }

    @SneakyThrows
    @Test
    void getInvoiceCountByType_negative() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        Customer customer3 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setType(InvoiceType.WHOLESALE);

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setType(InvoiceType.RETAIL);

        Invoice invoice3 = target.generateRandomInvoice(customer3);
        invoice3.setType(InvoiceType.RETAIL);

        target.setInvoices(List.of(invoice1, invoice2, invoice3));

        long actualRetailCount = target.getInvoiceCountByType(InvoiceType.RETAIL);
        long unexpectedRetailCount = 20;

        long actualWholesaleCount = target.getInvoiceCountByType(InvoiceType.WHOLESALE);
        long unexpectedWholesaleCount = 10;

        Assertions.assertNotEquals(unexpectedRetailCount, actualRetailCount);
        Assertions.assertNotEquals(unexpectedWholesaleCount, actualWholesaleCount);
    }

    @Test
    void getInvoicesSum() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone));

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setProductList(List.of(television));

        target.setInvoices(List.of(invoice1, invoice2));

        double actualSum = target.getInvoicesSum();
        double expectedSum = 30;

        Assertions.assertEquals(expectedSum, actualSum);

    }

    @Test
    void getInvoicesSum_negative() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone));

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setProductList(List.of(television));

        target.setInvoices(List.of(invoice1, invoice2));

        double actualSum = target.getInvoicesSum();
        double expectedSum = 31;

        Assertions.assertNotEquals(expectedSum, actualSum);

    }

    @Test
    void getInvoiceSumAndCustomerInfo() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone));

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setProductList(List.of(television));

        target.setInvoices(List.of(invoice1, invoice2));

        Map<Double, Customer> actualMap = new TreeMap<>();
        actualMap.put(telephone.getPrice(), customer1);
        actualMap.put(television.getPrice(), customer2);

        Map<Double, Customer> expectedMap = target.getInvoiceSumAndCustomerInfo();

        Assertions.assertEquals(expectedMap, actualMap);

    }

    @Test
    void getInvoiceSumAndCustomerInfo_negative() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone));

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setProductList(List.of(television));

        target.setInvoices(List.of(invoice1, invoice2));

        Map<Double, Customer> actualMap = target.getInvoiceSumAndCustomerInfo();

        Map<Double, Customer> unexpectedMap = new TreeMap<>();
        telephone.setPrice(11);
        unexpectedMap.put(telephone.getPrice(), customer1);
        unexpectedMap.put(television.getPrice(), customer2);

        Assertions.assertNotEquals(unexpectedMap, actualMap);

    }


    @Test
    void getSoldProductsCountByType() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone, television));

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setProductList(List.of(television));

        target.setInvoices(List.of(invoice1, invoice2));

        long actualTelephoneCount = target.getSoldProductsCountByType(ProductType.TELEPHONE);
        long expectedTelephoneCount = 1;

        long actualTelevisionCount = target.getSoldProductsCountByType(ProductType.TELEVISION);
        long expectedTelevisionCount = 2;

        Assertions.assertEquals(expectedTelephoneCount, actualTelephoneCount);
        Assertions.assertEquals(expectedTelevisionCount, actualTelevisionCount);

    }

    @Test
    void getSoldProductsCountByType_negative() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone, television));

        Invoice invoice2 = target.generateRandomInvoice(customer2);
        invoice2.setProductList(List.of(television));

        target.setInvoices(List.of(invoice1, invoice2));

        long actualTelephoneCount = target.getSoldProductsCountByType(ProductType.TELEPHONE);
        long unexpectedTelephoneCount = 2;

        long actualTelevisionCount = target.getSoldProductsCountByType(ProductType.TELEVISION);
        long unexpectedTelevisionCount = 1;

        Assertions.assertNotEquals(unexpectedTelephoneCount, actualTelephoneCount);
        Assertions.assertNotEquals(unexpectedTelevisionCount, actualTelevisionCount);

    }

    @Test
    void hasOnlyOneProductType_negative() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);
        Television television = new Television(20, Country.CHINA, "2-S", ScreenType.IPS, 30);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone, television));

        target.setInvoices(List.of(invoice1));

        boolean actual = target.hasOnlyOneProductType(invoice1.getProductList(), ProductType.TELEPHONE);
        boolean unexpected = true;

        Assertions.assertNotEquals(unexpected, actual);
    }

    @Test
    void hasOnlyOneProductType() {
        setUpProducts();

        Telephone telephone = new Telephone(10, "1-S", "Samsung", ScreenType.LED);

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        invoice1.setProductList(List.of(telephone));

        target.setInvoices(List.of(invoice1));

        boolean actual = target.hasOnlyOneProductType(invoice1.getProductList(), ProductType.TELEPHONE);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoicesByCustomersUnderAge() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        customer1.setAge(40);

        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        customer2.setAge(16);

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        Invoice invoice2 = target.generateRandomInvoice(customer2);

        target.setInvoices(List.of(invoice1, invoice2));

        List<Invoice> actual = target.getInvoicesByCustomersUnderAge(18, true);
        List<Invoice> expected = List.of(invoice2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getInvoicesByCustomersUnderAge_negative() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        customer1.setAge(40);

        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        customer2.setAge(16);

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        Invoice invoice2 = target.generateRandomInvoice(customer2);

        target.setInvoices(List.of(invoice1, invoice2));

        List<Invoice> actual = target.getInvoicesByCustomersUnderAge(18, true);
        List<Invoice> unexpected = List.of(invoice1);

        Assertions.assertNotEquals(unexpected, actual);
    }

    @Test
    void getFirstInvoices() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        Customer customer3 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        Invoice invoice2 = target.generateRandomInvoice(customer2);
        Invoice invoice3 = target.generateRandomInvoice(customer3);

        target.setInvoices(List.of(invoice1, invoice2, invoice3));

        List<Invoice> actual = target.getFirstInvoices(2);
        List<Invoice> expected = List.of(invoice1, invoice2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getFirstInvoices_negative() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        Customer customer3 = PersonService.getInstance().generateRandomCustomer();

        Invoice invoice1 = target.generateRandomInvoice(customer1);
        Invoice invoice2 = target.generateRandomInvoice(customer2);
        Invoice invoice3 = target.generateRandomInvoice(customer3);

        target.setInvoices(List.of(invoice1, invoice2, invoice3));

        List<Invoice> actual = target.getFirstInvoices(2);
        List<Invoice> unexpected = List.of(invoice1, invoice3);

        Assertions.assertNotEquals(unexpected, actual);
    }

    @Test
    void getSortedInvoices() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        customer1.setAge(90);
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        customer2.setAge(70);
        Customer customer3 = PersonService.getInstance().generateRandomCustomer();
        customer3.setAge(100);
        Invoice invoice1 = target.generateRandomInvoice(customer1);
        Invoice invoice2 = target.generateRandomInvoice(customer2);
        Invoice invoice3 = target.generateRandomInvoice(customer3);

        target.setInvoices(List.of(invoice1, invoice2, invoice3));

        List<Invoice> actual = target.getSortedInvoices();
        List<Invoice> expected = List.of(invoice3, invoice1, invoice2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getSortedInvoices_negative() {
        setUpProducts();

        Customer customer1 = PersonService.getInstance().generateRandomCustomer();
        customer1.setAge(90);
        Customer customer2 = PersonService.getInstance().generateRandomCustomer();
        customer2.setAge(70);
        Customer customer3 = PersonService.getInstance().generateRandomCustomer();
        customer3.setAge(100);
        Invoice invoice1 = target.generateRandomInvoice(customer1);
        Invoice invoice2 = target.generateRandomInvoice(customer2);
        Invoice invoice3 = target.generateRandomInvoice(customer3);

        target.setInvoices(List.of(invoice1, invoice2, invoice3));

        List<Invoice> actual = target.getSortedInvoices();
        List<Invoice> unexpected = List.of(invoice2, invoice3, invoice1);

        Assertions.assertNotEquals(unexpected, actual);
    }

}
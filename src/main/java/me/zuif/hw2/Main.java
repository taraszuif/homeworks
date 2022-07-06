package me.zuif.hw2;


import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.TeaService;

public class Main {
    private static final PhoneService PHONE_SERVICE = new PhoneService();
    private static final TeaService TEA_SERVICE = new TeaService();
    private static final PenService PEN_SERVICE = new PenService();

    public static void main(String[] args) {
        //PHONE
        PHONE_SERVICE.createAndSavePhones(10);
        System.out.println("Phones created:");
        PHONE_SERVICE.printAll();
        Phone phone = PHONE_SERVICE.getAll().get(2);
        System.out.println("Changing the price of phone with index 2 to 100 ");
        System.out.println("Before: " + phone);
        phone.setPrice(100);
        PHONE_SERVICE.update(phone);
        System.out.println("After: " + phone);
        System.out.println("Phones list:");
        PHONE_SERVICE.printAll();
        System.out.println("Deleting " + phone);
        PHONE_SERVICE.delete(phone.getId());
        System.out.println("Phones list:");
        PHONE_SERVICE.printAll();
        System.out.println("-----------------------------------");

        //TEA
        TEA_SERVICE.createAndSaveTeas(4);
        System.out.println("Teas created:");
        TEA_SERVICE.printAll();
        Tea tea = TEA_SERVICE.getAll().get(0);
        System.out.println("Changing title of tea with index 0 to 'Cool Tea' ");
        System.out.println("Before: " + tea);
        tea.setTitle("Cool Tea");
        TEA_SERVICE.update(tea);
        System.out.println("After: " + tea);
        System.out.println("Tea list:");
        TEA_SERVICE.printAll();
        System.out.println("Deleting " + tea);
        TEA_SERVICE.delete(tea.getId());
        System.out.println("Tea list:");
        TEA_SERVICE.printAll();
        System.out.println("-----------------------------------");

        //PEN
        PEN_SERVICE.createAndSavePens(5);
        System.out.println("Pens created:");
        PEN_SERVICE.printAll();
        Pen pen = PEN_SERVICE.getAll().get(4);
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

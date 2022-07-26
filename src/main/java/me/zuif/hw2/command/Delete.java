package me.zuif.hw2.command;

import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.ProductService;
import me.zuif.hw2.service.TeaService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;

import java.util.List;

public class Delete implements ICommand {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final TeaService TEA_SERVICE = TeaService.getInstance();
    private static final PenService PEN_SERVICE = PenService.getInstance();

    @Override
    public void execute() {
        System.out.println("What do you want to delete");
        ProductType[] types = ProductType.values();
        final List<String> names = Utils.getNamesOfEnum(types);
        int productTypeIndex = UserInputUtil.getUserInput(types.length, names);
        switch (types[productTypeIndex]) {
            case PHONE -> delete(PHONE_SERVICE);
            case PEN -> delete(PEN_SERVICE);
            case TEA -> delete(TEA_SERVICE);
            default -> throw new IllegalStateException("Unknown ProductType " + types[productTypeIndex]);
        }
    }

    private void delete(ProductService<? extends Product> service) {
        while (true) {
            System.out.println("Enter product ID");
            try {
                String id = SCANNER.nextLine();
                while (id.length() == 0) id = SCANNER.nextLine();
                service.findById(id);
                service.delete(id);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong ID. Try again");
            }
        }
    }
}

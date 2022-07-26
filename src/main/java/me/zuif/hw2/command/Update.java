package me.zuif.hw2.command;

import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.ProductService;
import me.zuif.hw2.service.TeaService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Update implements ICommand {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final TeaService TEA_SERVICE = TeaService.getInstance();
    private static final PenService PEN_SERVICE = PenService.getInstance();


    @Override
    public void execute() {
        System.out.println("What do you want to update:");
        ProductType[] types = ProductType.values();
        final List<String> names = getNamesOfType(types);
        int productTypeIndex = UserInputUtil.getUserInput(types.length, names);
        switch (types[productTypeIndex]) {
            case PHONE -> update(PHONE_SERVICE);
            case PEN -> update(PEN_SERVICE);
            case TEA -> update(TEA_SERVICE);
            default -> throw new IllegalStateException("Unknown ProductType " + types[productTypeIndex]);
        }
    }

    private List<String> getNamesOfType(final ProductType[] values) {
        final List<String> names = new ArrayList<>(values.length);
        for (ProductType type : values) {
            names.add(type.name());
        }
        return names;
    }

    private void update(ProductService<? extends Product> service) {
        while (true) {
            System.out.println("Enter product ID");
            try {
                String id = SCANNER.nextLine();
                while (id.length() == 0) id = SCANNER.nextLine();
                Product product = service.findById(id);
                final List<String> names = Arrays.asList("Update Title", "Update Price", "Update Count");
                int stop = 0;
                do {
                    int productTypeIndex = UserInputUtil.getUserInput(names.size(), names);
                    switch (names.get(productTypeIndex)) {
                        case "Update Title" -> updateTitle(product);
                        case "Update Price" -> updatePrice(product);
                        case "Update Count" -> updateCount(product);
                    }
                    System.out.println("Enter -1 to complete the update, or any other number to continue updating");
                    stop = SCANNER.nextInt();
                } while (stop != -1);
                service.update(product);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong ID. Try again");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updatePrice(Product product) throws IOException {
        System.out.println("Enter new Price");
        String price = SCANNER.nextLine();
        if (Utils.isNumeric(price)) {
            product.setPrice(Long.parseLong(price));
        } else {
            System.out.println("Wrong input");
            updatePrice(product);
        }
    }

    private void updateTitle(Product product) throws IOException {
        System.out.println("Enter new Title");
        String title = SCANNER.nextLine();
        product.setTitle(title);
    }

    private void updateCount(Product product) throws IOException {
        System.out.println("Enter new Count");
        String count = SCANNER.nextLine();
        if (Utils.isNumeric(count)) {
            product.setCount(Integer.parseInt(count));
        } else {
            System.out.println("Wrong input");
            updateCount(product);
        }
    }
}

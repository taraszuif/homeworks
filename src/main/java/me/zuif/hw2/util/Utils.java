package me.zuif.hw2.util;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static List<String> getNamesOfEnum(final Enum[] values) {
        final List<String> names = new ArrayList<>(values.length);
        for (Enum type : values) {
            names.add(type.name());
        }
        return names;
    }
}

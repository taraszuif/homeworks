package me.zuif.hw2.util.parser;

import java.io.InputStream;
import java.util.*;

public class JsonParser {

    public static List<String> jsonToLines(InputStream jsonInputStream) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(jsonInputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lines.add(line.trim());
        }
        return lines;
    }

    public static Map<String, String> jsonLinesToMap(List<String> lines) {
        Map<String, String> map = new HashMap<>();
        int layer = -1;
        String layername = "";
        for (String line : lines) {
            line = line.replaceAll("\"", "").replaceAll(",", "");
            if (line.contains("[") || line.contains("]")) {
                continue;
            }
            if (line.contains("{")) {
                ++layer;
                if (layer == 0) {
                    continue;
                }
                layername += line.replaceAll(" \\{", "").replaceAll(":", "") + ".";
                continue;
            }
            if (line.contains("}")) {
                --layer;
                if (layer == -1) {
                    continue;
                }
                String[] layers = layername.split("\\.");
                String newLayer = "";
                for (int i = 0; i < layers.length - 1; i++) {
                    if (i == 0) {
                        newLayer = layers[i];
                    } else {
                        newLayer += "." + layers[i];
                    }
                }
                layername = newLayer.trim();
                continue;
            }
            String[] strings = line.split(":");
            String key = strings[0].trim();
            String value = "";
            for (int i = 1; i < strings.length; i++) {
                if (i != strings.length - 1) {
                    value += strings[i] + ":";
                } else {
                    value += strings[i];
                }
            }
            if (layer != 0) {
                map.put(layername + key, value.trim());
            } else map.put(key, value.trim());
        }
        return map;
    }

}

package me.zuif.hw2.util.parser;

import java.io.InputStream;
import java.util.*;

public class XmlParser {

    public static List<String> xmlToLines(InputStream jsonInputStream) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(jsonInputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lines.add(line.trim());
        }
        return lines;
    }

    public static Map<String, String> xmlLinesToMap(List<String> lines) {
        Map<String, String> map = new HashMap<>();
        int layer = -1;
        String layername = "";
        for (String line : lines) {

            // System.out.println("line: " + line + " replaced: " + line.replaceAll(">.*<", ""));
            boolean layerString = line.length() == line.replaceAll(">.*<", "").length();
            if (!line.startsWith("<?") && layerString && !line.contains("</")) {
                ++layer;
                if (layer == 0) continue;
                layername += line.replaceAll("<", "").replaceAll(">", "") + ".";
                continue;
            }
            if (!line.startsWith("<?") && layerString && line.contains("</")) {
                --layer;
                if (layer == -1) continue;
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

            if (line.contains("<") && line.contains("</") && !line.startsWith("</")) {
                if (line.contains("currency")) {
                    String key = line.substring(line.indexOf('<') + 1, line.indexOf(" "));
                    String price = line.substring(line.indexOf('>') + 1, line.indexOf("</"));
                    map.put(layername + key, price);
                    String currency = line.substring(line.indexOf(" ") + 1, line.indexOf("="));
                    String value = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    map.put(layername + currency, value);
                } else {
                    String key = line.substring(line.indexOf('<') + 1, line.indexOf('>'));
                    String value = line.substring(line.indexOf('>') + 1, line.indexOf("</"));
                    map.put(layername + key, value);
                }
            }
        }
        return map;
    }
}

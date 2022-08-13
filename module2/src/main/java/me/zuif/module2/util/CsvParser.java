package me.zuif.module2.util;

import me.zuif.module2.exception.InvalidStringException;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CsvParser {
    private static CsvParser instance;
    private final Function<List<String>, List<Map<String, String>>> csvToMapList;

    private CsvParser() {
        csvToMapList = (lines) -> {
            List<Map<String, String>> result = new LinkedList<>();
            String[] columns = lines.get(0).split(",");
            lines.stream().skip(1).forEach(line -> {
                Map<String, String> column_value = new HashMap<>();
                IntStream.range(0, columns.length).forEach(i -> {
                    String[] args = line.split(",");
                    String value = args[i];
                    String column = columns[i];
                    if (value.length() == 0) {
                        try {
                            throw new InvalidStringException("Invalid input: column " + column + "(index=" + i + ")" +
                                    " in line " + line + " hasn't value");
                        } catch (InvalidStringException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        column_value.put(column, value);
                    }
                });
                result.add(column_value);
            });
            return result;
        };
    }

    public static CsvParser getInstance() {
        if (instance == null) {
            instance = new CsvParser();
        }
        return instance;
    }


    public List<String> csvToLines(InputStream jsonInputStream) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(jsonInputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lines.add(line.trim());
        }
        return lines;
    }

    public List<Map<String, String>> getCsvData(InputStream stream) {
        List<String> lines = csvToLines(stream);
        return csvToMapList.apply(lines);
    }

    public List<Map<String, String>> getCsvData(List<String> lines) {
        return csvToMapList.apply(lines);
    }
}

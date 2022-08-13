package me.zuif.module2.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CsvParserTest {
    CsvParser target;

    @BeforeEach
    void setUp() {
        target = CsvParser.getInstance();
    }

    @Test
    void getCsvData() {
        List<String> lines = new ArrayList<>();

        lines.add("type,series,model,diagonal,screen type,country,price");
        lines.add("Telephone,1-S,Samsung,none,LED,none,10");
        lines.add("Television,2-S,none,30,IPS,China,20");

        Map<String, String> expectedTelephoneMap = new HashMap<>();
        expectedTelephoneMap.put("country", "none");
        expectedTelephoneMap.put("series", "1-S");
        expectedTelephoneMap.put("price", String.valueOf(10));
        expectedTelephoneMap.put("screen type", "LED");
        expectedTelephoneMap.put("model", "Samsung");
        expectedTelephoneMap.put("type", "Telephone");
        expectedTelephoneMap.put("diagonal", "none");

        Map<String, String> expectedTelevisionMap = new HashMap<>();
        expectedTelevisionMap.put("country", "China");
        expectedTelevisionMap.put("series", "2-S");
        expectedTelevisionMap.put("price", String.valueOf(20));
        expectedTelevisionMap.put("screen type", "IPS");
        expectedTelevisionMap.put("model", "none");
        expectedTelevisionMap.put("type", "Television");
        expectedTelevisionMap.put("diagonal", "30");

        List<Map<String, String>> list = target.getCsvData(lines);
        Map<String, String> actualTelephoneMap = list.get(0);
        Map<String, String> actualTelevisionMap = list.get(1);

        Assertions.assertEquals(expectedTelephoneMap, actualTelephoneMap);
        Assertions.assertEquals(expectedTelevisionMap, actualTelevisionMap);
    }

    @Test
    void getCsvData_negative() {
        List<String> lines = new ArrayList<>();

        lines.add("type,series,model,diagonal,screen type,country,price");
        lines.add("Telephone,1-S,Samsung,none,LED,none,10");
        lines.add("Television,2-S,none,30,IPS,China,20");

        Map<String, String> unexpectedTelephoneMap = new HashMap<>();
        unexpectedTelephoneMap.put("country", "none");
        unexpectedTelephoneMap.put("series", "1-S");
        unexpectedTelephoneMap.put("price", String.valueOf(10));
        unexpectedTelephoneMap.put("screen type", "LED");
        unexpectedTelephoneMap.put("model", "Samsung");
        unexpectedTelephoneMap.put("type", "Telephone");
        unexpectedTelephoneMap.put("diagonal", "35");

        Map<String, String> unexpectedTelevisionMap = new HashMap<>();
        unexpectedTelevisionMap.put("country", "China");
        unexpectedTelevisionMap.put("series", "2-S");
        unexpectedTelevisionMap.put("price", String.valueOf(20));
        unexpectedTelevisionMap.put("screen type", "IPS");
        unexpectedTelevisionMap.put("model", "none");
        unexpectedTelevisionMap.put("type", "Television");
        unexpectedTelevisionMap.put("diagonal", "20");

        List<Map<String, String>> list = target.getCsvData(lines);
        Map<String, String> actualTelephoneMap = list.get(0);
        Map<String, String> actualTelevisionMap = list.get(1);

        Assertions.assertNotEquals(unexpectedTelephoneMap, actualTelephoneMap);
        Assertions.assertNotEquals(unexpectedTelevisionMap, actualTelevisionMap);
    }
}
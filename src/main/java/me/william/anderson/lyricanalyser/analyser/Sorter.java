package me.william.anderson.lyricanalyser.analyser;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

@SuppressWarnings("unused")
class Sorter {

    static LinkedHashMap<String, Integer> sortHashMapByValueAscending(LinkedHashMap<String, Integer> map) {
        return map
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    static LinkedHashMap<String, Integer> sortHashMapByValueDescending(LinkedHashMap<String, Integer> map) {
        return map
                .entrySet()
                .stream()
                .sorted(reverseOrder(comparingByValue()))
                .collect(
                        toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    static LinkedHashMap<String, Integer> sortHashMapByKeyAscending(LinkedHashMap<String, Integer> map) {
        return map
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .collect(
                        toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    static LinkedHashMap<String, Integer> sortHashMapByKeyDescending(LinkedHashMap<String, Integer> map) {
        return map
                .entrySet()
                .stream()
                .sorted(reverseOrder(comparingByKey()))
                .collect(
                        toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }
}

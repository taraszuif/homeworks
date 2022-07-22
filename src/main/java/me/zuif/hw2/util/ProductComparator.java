package me.zuif.hw2.util;

import me.zuif.hw2.model.Product;

import java.util.Comparator;

public class ProductComparator<T extends Product> implements Comparator<T> {
    @Override
    public int compare(T first, T second) {
        if (second.getPrice() == first.getPrice()) {
            if (first.getTitle().equals(second.getTitle())) {
                return Integer.compare(first.getCount(), second.getCount());
            }
            return first.getTitle().compareTo(second.getTitle());
        }
        return Double.compare(second.getPrice(), first.getPrice());
    }
}
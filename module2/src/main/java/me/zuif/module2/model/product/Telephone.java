package me.zuif.module2.model.product;

import java.util.Objects;

public class Telephone extends Product {
    private final String series;
    private final String model;
    private final ScreenType screenType;

    public Telephone(double price, String series, String model, ScreenType screenType) {
        super(ProductType.TELEPHONE, price);
        this.series = series;
        this.model = model;
        this.screenType = screenType;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", series='" + series + '\'' +
                ", model='" + model + '\'' +
                ", screenType=" + screenType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telephone telephone = (Telephone) o;
        return series.equals(telephone.series) && model.equals(telephone.model) && screenType == telephone.screenType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(series, model, screenType);
    }
}

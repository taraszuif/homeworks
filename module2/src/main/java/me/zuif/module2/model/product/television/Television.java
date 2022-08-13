package me.zuif.module2.model.product.television;

import lombok.Getter;
import lombok.Setter;
import me.zuif.module2.model.product.Product;
import me.zuif.module2.model.product.ProductType;
import me.zuif.module2.model.product.ScreenType;

import java.util.Objects;

@Getter
@Setter
public class Television extends Product {
    private Country country;
    private String series;
    private ScreenType screenType;
    private double diagonal;

    public Television(double price, Country country, String series, ScreenType screenType, double diagonal) {
        super(ProductType.TELEVISION, price);
        this.country = country;
        this.series = series;
        this.screenType = screenType;
        this.diagonal = diagonal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Television that = (Television) o;
        return Double.compare(that.diagonal, diagonal) == 0 && country == that.country && series.equals(that.series) && screenType == that.screenType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, series, screenType, diagonal);
    }

    @Override
    public String toString() {
        return "Television{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", country=" + country +
                ", series='" + series + '\'' +
                ", screenType=" + screenType +
                ", diagonal=" + diagonal +
                '}';
    }
}

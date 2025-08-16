package sample.cafekiosk.unit.beverage;

public class Americano implements Beverage {
    String name = "아메리카노";
    int price = 4000;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }
}

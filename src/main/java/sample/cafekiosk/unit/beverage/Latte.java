package sample.cafekiosk.unit.beverage;

public class Latte implements Beverage {
    String name = "라떼";
    int price = 4500;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }
}

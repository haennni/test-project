package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {
    public static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 00);
    public static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 00);

    private final List<Beverage> beverages = new ArrayList<>();


    public int calculateTotalPrice() {
        return beverages.stream()
                .mapToInt(Beverage::getPrice)
                .sum();
    }

    public void add(Beverage beverage, int count) {
        if (count <=  0) throw new IllegalArgumentException("음료는 한 잔 이상 주문해야합니다.");

        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }


    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public Order createOrder(LocalDateTime date) {
        LocalTime orderTime = date.toLocalTime();

        if (orderTime.isBefore(SHOP_OPEN_TIME) || orderTime.isAfter(SHOP_CLOSE_TIME))
            throw new IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.");

        return new Order(LocalDateTime.now(), beverages);
    }
}

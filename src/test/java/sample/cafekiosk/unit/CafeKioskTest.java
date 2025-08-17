package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CafeKioskTest {
    @Test
    void manual_add() {
        CafeKiosk cafekiosk = new CafeKiosk();
        cafekiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수: " + cafekiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료: " + cafekiosk.getBeverages().get(0).getName());
    }

    @Test
    void add() {
        CafeKiosk cafekiosk = new CafeKiosk();
        cafekiosk.add(new Americano());
        cafekiosk.add(new Latte());

        // 담긴 음료의 항목이 2개인지 검증
        // 1. 리스트의 사이즈가 2과 똑같은지 검증
        assertThat(cafekiosk.getBeverages().size()).isEqualTo(2);
        // 2. 리스트의 사이즈가 2인지 검증
        assertThat(cafekiosk.getBeverages()).hasSize(2);

        // 순서대로 담긴 음료의 이름 검증
        assertThat(cafekiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(cafekiosk.getBeverages().get(1).getName()).isEqualTo("라떼");
    }

    @Test
    void remove() {
        CafeKiosk cafekiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafekiosk.add(americano);

        // 음료의 총 주문 음료가 2개인지 검증
        assertThat(cafekiosk.getBeverages()).hasSize(1);

        // 음료 하나가 취소되어 총 주문 음료가 0개인지 검증
        cafekiosk.remove(americano);
        assertThat(cafekiosk.getBeverages()).hasSize(0);

        // 주문 내역이 비어있는지 검증
        assertThat(cafekiosk.getBeverages()).isEmpty();
    }
    @Test
    void clear() {
        CafeKiosk cafekiosk = new CafeKiosk();
        cafekiosk.add(new Americano());
        cafekiosk.add(new Latte());

        assertThat(cafekiosk.getBeverages()).hasSize(2);

        // 키오스크 음료 모두 초기화
        cafekiosk.clear();
        assertThat(cafekiosk.getBeverages()).hasSize(0);
        assertThat(cafekiosk.getBeverages()).isEmpty();
    }

    //해피 케이스 작성
    @Test
    void addSeveralBeverages() {
        CafeKiosk cafekiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafekiosk.add(americano, 2);

        assertThat(cafekiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafekiosk.getBeverages().get(1)).isEqualTo(americano);

    assertThat(cafekiosk.getBeverages()).hasSize(2);
    }

    //예외 케이스 작성
    @Test
    void addZeroBeverage() {
        CafeKiosk cafekiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafekiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 한 잔 이상 주문해야합니다.");
    }

    //해피 케이스 작성
    @Test
    void successCreateOrder() {
        CafeKiosk cafekiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafekiosk.add(americano, 2);

        Order order = cafekiosk.createOrder();

        // 주문의 음료 리스트가 비어있지않아야한다.
        assertThat(order.getOrderItem()).isNotNull();
        // 주문의 첫 번째 음료 리스트는 아메리카노이다.
        assertThat(order.getOrderItem().get(0).getName()).isEqualTo("아메리카노");
        // 주문의 음료는 총 2개이다.
        assertThat(order.getOrderItem()).hasSize(2);
    }

    @Test
    void falseCreateOrder() {
        CafeKiosk cafekiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafekiosk.add(americano, 2);

        Order order = cafekiosk.createOrder();

        // 주문의 음료 리스트가 비어있지않아야한다.
        assertThat(order.getOrderItem()).isNotNull();
        // 주문의 첫 번째 음료 리스트는 아메리카노이다.
        assertThat(order.getOrderItem().get(0).getName()).isEqualTo("아메리카노");
        // 주문의 음료는 총 2개이다.
        assertThat(order.getOrderItem()).hasSize(2);
    }
}
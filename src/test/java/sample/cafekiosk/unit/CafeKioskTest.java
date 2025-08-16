package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;

class CafeKioskTest {

    @Test
    void add() {
        CafeKiosk cafekiosk = new CafeKiosk();
        cafekiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수: " + cafekiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료: " + cafekiosk.getBeverages().get(0).getName());
    }

}
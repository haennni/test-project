package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AmericanoTest {

    @Test
    void getName() {
        Americano americano = new Americano();
        //assertEquals("아메리카노", americano.getName());
        assertThat(americano.name).isEqualTo("아메리카노").isNotNull();
    }

    @Test
    void getPrice() {
        Americano americano = new Americano();
        assertThat(americano.price).isEqualTo(4000);
    }
}
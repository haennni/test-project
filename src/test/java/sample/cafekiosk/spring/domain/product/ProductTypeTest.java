package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지 체크한다.")
    @Test
    void containsStockType(){
        // given
        ProductType falseType = ProductType.HANDMADE;
        ProductType successType = ProductType.BOTTLE;

        // when
        boolean falseTypeResult = ProductType.containsStockType(falseType);
        boolean successTypeResult = ProductType.containsStockType(successType);

        // then
        assertFalse(falseTypeResult);
        assertTrue(successTypeResult);
    }

}
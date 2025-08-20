package sample.cafekiosk.spring.domain.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class OrderTest {
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @DisplayName("주문 생성 시, 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calcTotalPrice(){
        // given
        List<Product> products = List.of(createProduct("001", ProductType.BAKERY, 2000),
                createProduct("002", ProductType.BOTTLE, 4000),
                createProduct("003", ProductType.HANDMADE, 6000));
        // when
        Order order = Order.create(products, LocalDateTime.now());
        // then
        assertThat(order.getTotalPrice()).isEqualTo(12000);
    }

    @DisplayName("주문 생성 시, 주문 초기 상태는 INIT이다.")
    @Test
    void initProductStatus(){
        // given
        List<Product> products = List.of(createProduct("001", ProductType.BAKERY, 2000));

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
        /*isEqualByComparingTo -> ENUM값을 비교할 때 많이 쓰는 메서드이다.
           ENUM값 자체를 비교한다.*/

    }

    @DisplayName("주문 생성 시, 주문 등록 시간을 넣어준다.")
    @Test
    void test(){
        // given
        List<Product> products = List.of(
                createProduct("001", ProductType.BAKERY, 2000),
                createProduct("002", ProductType.HANDMADE, 4000));

        // when
        Order order = Order.create(
                products,
                LocalDateTime.of(2025, 8, 20, 11, 0, 0));

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 20, 11, 0, 0));
    }

    @DisplayName("주문 생성 시, 상품 리스트가 주문에 들어간다.")
    @Test
    void addProducts(){
        // given
        List<Product> products = List.of(
                createProduct("001", ProductType.BAKERY, 2000),
                createProduct("002", ProductType.HANDMADE, 4000));

        // when
        Order order = Order.create(
                products,
                LocalDateTime.of(2025, 8, 20, 11, 0, 0));

        // then
        assertThat(order.getOrderProducts()).hasSize(2)
                .extracting("product.productNumber", "product.price")
                .containsExactlyInAnyOrder(Tuple.tuple("001", 2000), Tuple.tuple("002", 4000));
    }

    private Product createProduct(String productNumber, ProductType type, int price) {
        Product product = Product.create(
                productNumber,
                type,
                SellingStatus.SELLING,
                "메뉴 이름",
                price);
        productRepository.save(product);

        return product;
    }

}
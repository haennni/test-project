package sample.cafekiosk.spring.domain.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.order.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.dto.OrderCreateRequest;
import sample.cafekiosk.spring.domain.product.dto.OrderResponse;
import sample.cafekiosk.spring.domain.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService; //테스트 하고자 하는 클래스

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder(){
        // given
        createProduct("001", ProductType.HANDMADE, 1000);
        createProduct("002", ProductType.HANDMADE, 3000);
        createProduct("003", ProductType.HANDMADE, 4000);

        /* 주문 생성 */
        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002"));
        LocalDateTime now = LocalDateTime.now();
        // when

        OrderResponse orderResponse = orderService.createOrder(request, now);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(now, 4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("002", 3000)
                );
    }

    @DisplayName("주문 리스트에 같은 종류의 음료를 받아 주문을 생성한다.")
    @Test
    void duplicateProductOrder(){
        // given
        createProduct("001", ProductType.HANDMADE, 1000);
        createProduct("002", ProductType.HANDMADE, 3000);
        createProduct("003", ProductType.HANDMADE, 4000);

        /* 주문 생성 */
        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001"));
        LocalDateTime now = LocalDateTime.now();
        // when

        OrderResponse orderResponse = orderService.createOrder(request, now);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(now, 2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("001", 1000)
                );
    }



    private void createProduct(String productNumber, ProductType type, int price) {
        Product product = Product.create(
                productNumber,
                type,
                SellingStatus.SELLING,
                "메뉴 이름",
                price);
        productRepository.save(product);
    }

}
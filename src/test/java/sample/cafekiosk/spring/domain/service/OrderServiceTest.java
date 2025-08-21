package sample.cafekiosk.spring.domain.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.order.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.*;
import sample.cafekiosk.spring.domain.product.dto.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.product.dto.OrderResponse;
import sample.cafekiosk.spring.api.service.order.OrderService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Autowired
    StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder(){
        // given
        createProduct("001", ProductType.HANDMADE, 1000);
        createProduct("002", ProductType.HANDMADE, 3000);
        createProduct("003", ProductType.HANDMADE, 4000);

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

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock(){
        // given
        createProduct("001", ProductType.BAKERY, 1000);
        createProduct("002", ProductType.BOTTLE, 3000);
        createProduct("003", ProductType.HANDMADE, 4000);

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002", "001", "003"));
        LocalDateTime now = LocalDateTime.now();
        // when

        OrderResponse orderResponse = orderService.createOrder(request, now);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(now, 9000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("002", 3000),
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("003", 4000)
                );
        List<Stock> stocks = stockRepository.findAll();

        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 0),
                        Tuple.tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품이 포함되어 있는 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock(){
        // given
        createProduct("001", ProductType.BAKERY, 1000);
        createProduct("002", ProductType.BOTTLE, 3000);
        createProduct("003", ProductType.HANDMADE, 4000);

        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002", "001", "003"));
        LocalDateTime now = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
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

package sample.cafekiosk.spring.domain.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.*;
import sample.cafekiosk.spring.api.controller.product.dto.response.ProductResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 번호에서 1 증가한 값이다.")
    @Test
    void createProduct(){
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, SellingStatus.SELLING, "아메리카노", 1000);
        Product product2 = createProduct("002", ProductType.HANDMADE, SellingStatus.HOLD, "카페라떼",3000);
        Product product3 = createProduct("003", ProductType.HANDMADE, SellingStatus.STOP_SELLING, "팥빙수",4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse product = productService.createProduct(request);

        // then
        assertThat(product)
                .extracting("productNumber", "productType", "sellingStatus", "name", "price")
                .contains("004", ProductType.HANDMADE, SellingStatus.SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(4)
                .extracting("productNumber", "productType", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", ProductType.HANDMADE, SellingStatus.SELLING, "아메리카노", 1000),
                        Tuple.tuple("002", ProductType.HANDMADE, SellingStatus.HOLD, "카페라떼", 3000),
                        Tuple.tuple("003", ProductType.HANDMADE, SellingStatus.STOP_SELLING, "팥빙수", 4000),
                        Tuple.tuple("004", ProductType.HANDMADE, SellingStatus.SELLING, "카푸치노", 5000));
    }

    @DisplayName("신규 상품을 등록할 때. 샌규 상품을 등록하면 상품번호는 001이다.")
    @Test
    void createProductWhenProductsIsEmpty(){
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .sellingStatus(SellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse product = productService.createProduct(request);

        // then
        assertThat(product)
                .extracting("productNumber", "productType", "sellingStatus", "name", "price")
                .contains("001", ProductType.HANDMADE, SellingStatus.SELLING, "카푸치노", 5000);
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

    private Product createProduct(String productNumber, ProductType type, SellingStatus status, String name, int price) {
        Product product = Product.create(
                productNumber,
                type,
                status,
                name,
                price);
        productRepository.save(product);

        return product;
    }

}
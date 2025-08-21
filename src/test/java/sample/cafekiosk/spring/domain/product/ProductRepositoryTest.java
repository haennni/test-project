package sample.cafekiosk.spring.domain.product;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ActiveProfiles("test")
@SpringBootTest // 스프링에서 제공하는 통합 테스트를 위한 애노테이션
// @DataJpaTest
// 스프링 서버를 띄워서 테스트하는데, SpringBootTest보다 가볍다. JPA와 관련된 빈만 주입하기 떄문이다.
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void testClear() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void getSellingProductList(){
        // given
        Product product1 = Product.create(
                "001",
                ProductType.HANDMADE,
                SellingStatus.SELLING,
                "아메리카노",
                4000);
        productRepository.save(product1);

        Product product2 = Product.create(
                "002",
                ProductType.HANDMADE,
                SellingStatus.HOLD,
                "카페라떼",
                4500);
        productRepository.save(product2);

        Product product3 = Product.create(
                "003",
                ProductType.BAKERY,
                SellingStatus.STOP_SELLING,
                "팥빙수",
                7000);
        productRepository.save(product3);


        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SellingStatus.SELLING, SellingStatus.HOLD));

        // then
        //리스트에 대한 검증
        assertThat(products).hasSize(2) // 사이즈 검증
                .extracting("productNumber", "name", "sellingStatus") //선택한 필드만 추출해서
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SellingStatus.SELLING),
                        tuple("002", "카페라떼", SellingStatus.HOLD)
                );
        //.containsExactly() //포함되고, 순서까지 일치하는 지 확인*/


    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다")
    @Test
    void findAllByProductNumberIn(){
        // given
        createProduct("001", ProductType.HANDMADE, 1000);
        createProduct("002", ProductType.HANDMADE, 3000);
        createProduct("003", ProductType.HANDMADE, 4000);

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(Tuple.tuple("001", "메뉴 이름", 1000),
                        Tuple.tuple("002", "메뉴 이름", 3000));
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void findLatestProductNumber(){
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, SellingStatus.SELLING, "아메리카노", 1000);
        Product product2 = createProduct("002", ProductType.HANDMADE, SellingStatus.HOLD, "카페라떼",3000);
        Product product3 = createProduct("003", ProductType.HANDMADE, SellingStatus.STOP_SELLING, "팥빙수",4000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String lastestProduct = productRepository.findLastestProduct();

        // then
        assertThat(lastestProduct).isEqualTo("003");
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다. 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty(){
        // given
        // when
        String lastestProduct = productRepository.findLastestProduct();

        // then
        assertThat(lastestProduct).isNull();

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


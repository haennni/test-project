package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
}
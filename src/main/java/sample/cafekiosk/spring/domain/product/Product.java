package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    private SellingStatus sellingStatus;

    private String name;

    private int price;

    private Product(String productNumber, ProductType productType, SellingStatus sellingStatus, String name, int price) {
        this.productNumber = productNumber;
        this.productType = productType;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static Product create(String productNumber, ProductType productType, SellingStatus sellingStatus, String name, int price) {
        return new Product(productNumber, productType, sellingStatus, name, price);
    }
}

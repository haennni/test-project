package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType productType;
    private SellingStatus sellingType;
    private String name;
    private int price;

    private ProductResponse(Long id, String productNumber, ProductType productType, SellingStatus sellingType, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.productType = productType;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductNumber(),
                product.getProductType(),
                product.getSellingStatus(),
                product.getName(),
                product.getPrice()
        );
    }
}

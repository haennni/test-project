package sample.cafekiosk.spring.domain.product.dto;

import lombok.Getter;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType productType;
    private SellingStatus sellingStatus;
    private String name;
    private int price;

    private ProductResponse(Long id, String productNumber, ProductType productType, SellingStatus sellingStatus, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.productType = productType;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    private ProductResponse(String productNumber, ProductType productType, SellingStatus sellingStatus, String name, int price) {
        this.productNumber = productNumber;
        this.productType = productType;
        this.sellingStatus = sellingStatus;
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

    public static ProductResponse of(String productNumber, Product product) {
        return new ProductResponse(
                product.getId(),
                productNumber,
                product.getProductType(),
                product.getSellingStatus(),
                product.getName(),
                product.getPrice()
        );
    }

}

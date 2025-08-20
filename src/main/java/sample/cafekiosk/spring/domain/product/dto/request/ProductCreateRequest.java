package sample.cafekiosk.spring.domain.product.dto.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.product.SellingStatus;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private ProductType productType;

    private SellingStatus sellingStatus;

    private String name;

    private int price;

    public ProductCreateRequest(ProductType productType, SellingStatus sellingStatus, String name, int price) {
        this.productType = productType;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String number) {
        return Product.create(number, productType, sellingStatus, name, price);
    }
}

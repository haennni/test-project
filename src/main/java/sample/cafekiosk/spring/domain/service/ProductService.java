package sample.cafekiosk.spring.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductResponse;
import sample.cafekiosk.spring.domain.product.SellingStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products =
                productRepository.findAllBySellingStatusIn(SellingStatus.forDisplay());

        return products.stream().map(ProductResponse::of).toList();
    }
}

package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.dto.ProductResponse;
import sample.cafekiosk.spring.domain.product.SellingStatus;
import sample.cafekiosk.spring.domain.product.dto.request.ProductCreateRequest;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public List<ProductResponse> getSellingProducts() {
        List<Product> products =
                productRepository.findAllBySellingStatusIn(SellingStatus.forDisplay());

        return products.stream().map(ProductResponse::of).toList();
    }

    public ProductResponse createProduct(ProductCreateRequest request) {
        String productNumber = createNextProductNumber();

        Product product = request.toEntity(productNumber);
        productRepository.save(product);

        return ProductResponse.of(productNumber, product);
    }

    private String createNextProductNumber() {
        String lastestProductNumber = productRepository.findLastestProduct();
        if (lastestProductNumber == null) {
            return "001";}
        Integer lastestProductNumberInt = Integer.parseInt(lastestProductNumber) + 1;

        return String.format("%03d", lastestProductNumberInt);
    }
}

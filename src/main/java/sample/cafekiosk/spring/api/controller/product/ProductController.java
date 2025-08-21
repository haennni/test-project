package sample.cafekiosk.spring.api.controller.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.dto.response.ProductResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.of(HttpStatus.OK, "성공적으로 조회하였습니다.", productService.getSellingProducts());
    }

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.of(HttpStatus.OK, "성공적으로 생성하였습니다.", productService.createProduct(request));
    }
}

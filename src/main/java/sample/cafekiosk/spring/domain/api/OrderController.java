package sample.cafekiosk.spring.domain.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.domain.product.dto.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.product.dto.OrderResponse;
import sample.cafekiosk.spring.domain.service.OrderService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        LocalDateTime time = LocalDateTime.now();
        return ApiResponse.of(HttpStatus.OK, "성공적으로 주문을 생성하였습니다.", orderService.createOrder(request, time));

    }
}

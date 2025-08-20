package sample.cafekiosk.spring.domain.api;

import lombok.RequiredArgsConstructor;
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
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime time = LocalDateTime.now();
        return orderService.createOrder(request, time);

    }
}

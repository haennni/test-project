package sample.cafekiosk.spring.domain.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.domain.product.dto.OrderCreateRequest;
import sample.cafekiosk.spring.domain.service.OrderService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("new")
    public void createOrder(OrderCreateRequest request) {
        LocalDateTime time = LocalDateTime.now();
        orderService.createOrder(request, time);

    }
}

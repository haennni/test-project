package sample.cafekiosk.spring.domain.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.domain.product.OrderCreateRequest;
import sample.cafekiosk.spring.domain.product.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("new")
    public void createOrder(OrderCreateRequest request) {
        orderService.createOrder(request);
    }
}

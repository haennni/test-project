package sample.cafekiosk.spring.api.controller.order.dto.response;

import lombok.Getter;
import sample.cafekiosk.spring.api.controller.product.dto.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {
    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> products;

    private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getRegisteredDateTime(),
                order.getOrderProducts()
                        .stream()
                        .map(orderProduct ->
                                        ProductResponse.of(orderProduct.getProduct()))
                        .toList()

        );
    }

}

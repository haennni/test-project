package sample.cafekiosk.spring.api.service.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {
    private List<String> productNumbers;

    public OrderCreateServiceRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}

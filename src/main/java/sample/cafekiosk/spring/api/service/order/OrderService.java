package sample.cafekiosk.spring.api.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.order.dto.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.*;
import sample.cafekiosk.spring.api.controller.order.dto.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Transactional
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductsBy(productNumbers);
        /**
         * 재고 감소 -> 동시성 문제 (동시성에 대한 문제)
         * 데이터에 대한 lock을 잡고 순차적으로 처리될 수 있도록 진행해야함
         * optimistic lock / pessimistic lock / ...
         */

        deductStockQuantities(products);

        Order order = Order.create(products,  registeredDateTime);
        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    private void deductStockQuantities(List<Product> products) {
        // 1. 재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        // 2. 재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = createStockMapBy(stocks);

        // 3. 상품별 counting
        Map<String, Long> stockCountingMap = createCountingMapBy(stockProductNumbers);

        // 4. 재고 차감 시도
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)){
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = stockCountingMap.get(stockProductNumber).intValue();
            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    private static Map<String, Stock> createStockMapBy(List<Stock> stocks) {
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getProductType()))
                .map(Product::getProductNumber)
                .toList();
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMap::get)
                .toList();
    }
}

package cleanhouse.orderservice.order.adapter.in.presentation.controller;

import cleanhouse.orderservice.order.domain.application.port.out.KafkaProducerPort;
import cleanhouse.orderservice.order.domain.application.service.CreateOrderCommand;
import cleanhouse.orderservice.order.domain.application.dto.OrderCreateRequest;
import cleanhouse.orderservice.order.domain.application.dto.OrderListResponse;
import cleanhouse.orderservice.order.domain.application.port.in.OrderCreateUsecase;
import cleanhouse.orderservice.order.domain.application.port.in.OrderQueryUsecase;
import cleanhouse.orderservice.order.domain.application.service.GetOrdersByUserIdQuery;
import cleanhouse.orderservice.order.domain.application.service.GetOrdersQuery;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderController {
    private final OrderCreateUsecase orderCreateUsecase;
    private final OrderQueryUsecase orderQueryUsecase;
    private final KafkaProducerPort kafkaProducerPort;

    @Autowired
    public OrderController(
            @Qualifier("orderCreateKafkaService") OrderCreateUsecase orderCreateUsecase,
            OrderQueryUsecase orderQueryUsecase,
            KafkaProducerPort kafkaProducerPort) {
        this.orderCreateUsecase = orderCreateUsecase;
        this.orderQueryUsecase = orderQueryUsecase;
        this.kafkaProducerPort = kafkaProducerPort;
    }

    @PostMapping("/orders")
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderCreateRequest request) {
        log.info("Creating order for userId: {}, productId: {}", request.getUserId(), request.getProductId());
        try {
            Long orderId = orderCreateUsecase.create(CreateOrderCommand.from(request));

            return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid order request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderListResponse> getOrders() {
        log.info("Fetching all orders");
        GetOrdersQuery query = new GetOrdersQuery();
        OrderListResponse response = orderQueryUsecase.getOrders(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<OrderListResponse> getOrdersByUserId(@PathVariable String userId) {
        log.info("Fetching orders for userId: {}", userId);
        GetOrdersByUserIdQuery query = new GetOrdersByUserIdQuery(userId);
        OrderListResponse response = orderQueryUsecase.getOrdersByUserId(query);
        return ResponseEntity.ok(response);
    }
}

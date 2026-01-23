package cleanhouse.orderservice.order.adapter.in.presentation.controller;

import cleanhouse.orderservice.order.domain.application.port.out.KafkaProducerPort;
import cleanhouse.orderservice.order.domain.application.service.CreateOrderCommand;
import cleanhouse.orderservice.order.domain.application.dto.OrderCreateRequest;
import cleanhouse.orderservice.order.domain.application.dto.OrderListResponse;
import cleanhouse.orderservice.order.domain.application.port.in.OrderCreateUsecase;
import cleanhouse.orderservice.order.domain.application.port.in.OrderQueryUsecase;
import cleanhouse.orderservice.order.domain.application.service.GetOrdersByUserIdQuery;
import cleanhouse.orderservice.order.domain.application.service.GetOrdersQuery;
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
@RequiredArgsConstructor
public class OrderController {
    @Value("${kafka.topic.catalog}")
    private String catalogTopic;

    private final OrderCreateUsecase orderCreateUsecase;
    private final OrderQueryUsecase orderQueryUsecase;

    @PostMapping("/orders")
    public ResponseEntity<Long> createOrder(@RequestBody OrderCreateRequest request) {
        log.info("Creating order for userId: {}, productId: {}", request.getUserId(), request.getProductId());
        try {
            Long orderId = orderCreateUsecase.create(CreateOrderCommand.from(request));
            kafkaProducerPort.send(catalogTopic, request); // TODO: 컨트롤러 에서 orderUsecase랑 kafkaProducer를 모두 호출하고있는게 마음에 안듦. 그렇다고 orderUsecase에 kafka 보내는 로직을 넣는게 맞는건지도 헷갈림

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

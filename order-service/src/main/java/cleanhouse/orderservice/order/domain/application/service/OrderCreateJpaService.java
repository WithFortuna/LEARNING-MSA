package cleanhouse.orderservice.order.domain.application.service;

import java.math.BigDecimal;

import cleanhouse.orderservice.order.domain.application.port.in.OrderCreateUsecase;
import cleanhouse.orderservice.order.domain.application.port.out.KafkaProducerPort;
import cleanhouse.orderservice.order.domain.entity.Order;
import cleanhouse.orderservice.order.domain.application.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderCreateJpaService implements OrderCreateUsecase {
    @Value("${kafka.topic.catalog}")
    private String catalogTopic;

    private final OrderRepository orderRepository;
    private final KafkaProducerPort kafkaProducerPort;

    @Override
    @Transactional
    public Long create(CreateOrderCommand command) {
        if (command.getUserId() == null || command.getUserId().isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (command.getProductId() == null || command.getProductId().isBlank()) {
            throw new IllegalArgumentException("productId is required");
        }
        if (command.getQuantity() == null) {
            throw new IllegalArgumentException("quantity is required");
        }

        Order order = buildOrder(command);

        Order savedOrder = orderRepository.save(order);

        synchronizeProductQuantity(command);

        return savedOrder.getId();
    }

    private static Order buildOrder(CreateOrderCommand command) {
        return Order.create(
            command.getUserId(),
            command.getProductId(),
            command.getPrice().multiply(BigDecimal.valueOf(command.getQuantity())),
            command.getQuantity()
        );
    }

    private void synchronizeProductQuantity(CreateOrderCommand command) {
        kafkaProducerPort.send(catalogTopic, command);
    }
}

package cleanhouse.orderservice.order.domain.application.service;

import java.math.BigDecimal;

import cleanhouse.orderservice.order.domain.application.port.in.OrderCreateUsecase;
import cleanhouse.orderservice.order.domain.entity.Order;
import cleanhouse.orderservice.order.domain.application.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements OrderCreateUsecase {
    private final OrderRepository orderRepository;

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

        Order order = Order.create(
            command.getUserId(),
            command.getProductId(),
            command.getPrice().multiply(BigDecimal.valueOf(command.getQuantity())),
            command.getQuantity()
        );

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }
}

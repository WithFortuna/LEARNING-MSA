package cleanhouse.orderservice.order.domain.application.port.out;

import cleanhouse.orderservice.order.domain.entity.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);
    List<Order> findAll();
    List<Order> findByUserId(String userId);
}

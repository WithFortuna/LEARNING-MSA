package cleanhouse.orderservice.order.domain.application.service;

import cleanhouse.orderservice.order.domain.application.dto.OrderListResponse;
import cleanhouse.orderservice.order.domain.application.dto.OrderResponse;
import cleanhouse.orderservice.order.domain.application.port.in.OrderQueryUsecase;
import cleanhouse.orderservice.order.domain.application.port.out.OrderRepository;
import cleanhouse.orderservice.order.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderQueryHandler implements OrderQueryUsecase {
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getOrders(GetOrdersQuery query) {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, orderResponses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListResponse getOrdersByUserId(GetOrdersByUserIdQuery query) {
        List<Order> orders = orderRepository.findByUserId(query.getUserId());

        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return new OrderListResponse(orderResponses, orderResponses.size());
    }
}

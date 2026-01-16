package cleanhouse.orderservice.order.adapter.out.persistence;

import cleanhouse.orderservice.order.domain.entity.Order;
import cleanhouse.orderservice.order.domain.application.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderSpringDataRepository springDataRepository;
    @Qualifier("orderModelMapper")
    private final ModelMapper modelMapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = modelMapper.map(order, OrderEntity.class);
        OrderEntity savedEntity = springDataRepository.save(entity);
        return modelMapper.map(savedEntity, Order.class);
    }

    @Override
    public List<Order> findAll() {
        return springDataRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Order.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return springDataRepository.findByUserId(userId).stream()
                .map(entity -> modelMapper.map(entity, Order.class))
                .collect(Collectors.toList());
    }
}

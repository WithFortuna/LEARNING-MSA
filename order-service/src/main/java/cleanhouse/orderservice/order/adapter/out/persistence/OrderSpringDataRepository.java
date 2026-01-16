package cleanhouse.orderservice.order.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderSpringDataRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(String userId);
}

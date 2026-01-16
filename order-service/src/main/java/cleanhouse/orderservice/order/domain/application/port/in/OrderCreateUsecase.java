package cleanhouse.orderservice.order.domain.application.port.in;

import cleanhouse.orderservice.order.domain.application.service.CreateOrderCommand;

public interface OrderCreateUsecase {
    Long create(CreateOrderCommand command);
}

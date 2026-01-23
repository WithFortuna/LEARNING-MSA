package cleanhouse.orderservice.order.adapter.out.kafka.dto;

import java.time.ZoneOffset;

import cleanhouse.orderservice.order.domain.entity.Order;

public record OrderCreatePayload(
	long created_at,
	String user_id,
	String product_id,
	int quantity,
	double total_price
) {
	public static OrderCreatePayload from(Order order) {
		return new OrderCreatePayload(
			order.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli(),
			order.getUserId(),
			order.getProductId(),
			order.getQuantity(),
			order.getTotalPrice().doubleValue()
		);
	}
}

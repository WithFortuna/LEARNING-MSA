package cleanhouse.orderservice.order.domain.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cleanhouse.orderservice.order.adapter.out.kafka.dto.KafkaMessage;
import cleanhouse.orderservice.order.adapter.out.kafka.dto.OrderCreatePayload;
import cleanhouse.orderservice.order.adapter.out.kafka.dto.KafkaSchema;
import cleanhouse.orderservice.order.adapter.out.kafka.dto.KafkaSchema.Field;
import cleanhouse.orderservice.order.domain.application.port.in.OrderCreateUsecase;
import cleanhouse.orderservice.order.domain.application.port.out.KafkaProducerPort;
import cleanhouse.orderservice.order.domain.application.port.out.OrderRepository;
import cleanhouse.orderservice.order.domain.entity.Order;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderCreateKafkaService implements OrderCreateUsecase {
	@Value("${kafka.topic.order.create}")
	private String orderTopic;

	private final OrderRepository orderRepository;
	private final KafkaProducerPort kafkaProducerPort;

	@Override
	@Transactional
	public Long create(CreateOrderCommand command) {
		Order order = buildOrder(command);

		List<Field> fields = buildTableFields();
		KafkaSchema schema = buildKafkaSchema(fields);
		OrderCreatePayload payload = OrderCreatePayload.from(order);

		KafkaMessage kafkaMessage = new KafkaMessage(schema, payload);

		kafkaProducerPort.send(orderTopic, kafkaMessage);

		return 1L;	// TODO: 카프카로 데이터 삽입할 때는 반환값 id 어떻게?
	}

	private static KafkaSchema buildKafkaSchema(List<Field> fields) {
		return new KafkaSchema(
			"struct",
			fields,
			false,
			"orders"
		);
	}

	private static List<Field> buildTableFields() {
		List<Field> fields = List.of(
			new Field("int64", false, "created_at", "org.apache.kafka.connect.data.Timestamp"),
			new Field("string", false, "user_id", null),
			new Field("string", false, "product_id",null),
			new Field("int32", false, "quantity",null),
			new Field("double", false, "total_price",null)
		);
		return fields;
	}

	private static Order buildOrder(CreateOrderCommand command) {
		return Order.create(
			command.getUserId(),
			command.getProductId(),
			command.getPrice().multiply(BigDecimal.valueOf(command.getQuantity())),
			command.getQuantity()
		);
	}
}

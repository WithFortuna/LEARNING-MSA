package cleanhouse.orderservice.order.domain.application.port.out;

public interface KafkaProducerPort {
	void send(String topic, Object message);
}

package cleanhouse.orderservice.order.adapter.out.kafka.dto;

/**
 * Kafka topic에 보낼/가져올 메시지 클래스
 * @param schema 테이블 정보
 * @param payload 해당 테이블의 레코드
 */
public record KafkaMessage(
	KafkaSchema schema,
	OrderCreatePayload payload
) {
}

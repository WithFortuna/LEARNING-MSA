package cleanhouse.orderservice.order.adapter.out.kafka.dto;

import java.util.List;

public record KafkaSchema(
	String type,
	List<Field> fields,
	boolean optional,
	String name

) {

	public record Field(
		String type,
		boolean optional,
		String field,
		String name
	) {}
}



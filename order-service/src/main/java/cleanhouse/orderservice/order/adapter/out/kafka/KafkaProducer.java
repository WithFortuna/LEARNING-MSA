package cleanhouse.orderservice.order.adapter.out.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cleanhouse.orderservice.order.domain.application.port.out.KafkaProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer implements KafkaProducerPort {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void send(String topic, Object message) {
		String jsonInString = null;

		try {
			jsonInString = objectMapper.writeValueAsString(message);
		} catch (JsonProcessingException exception) {
			log.error("Error while converting message to JSON: {}", exception.getMessage());
			return;
		}

		kafkaTemplate.send(topic, jsonInString);
		log.info("Message sent to topic: {}. Message: {}", topic, jsonInString);
	}
}

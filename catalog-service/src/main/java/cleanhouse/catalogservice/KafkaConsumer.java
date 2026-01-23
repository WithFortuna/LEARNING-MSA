package cleanhouse.catalogservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cleanhouse.catalogservice.catalog.domain.entity.Catalog;
import cleanhouse.catalogservice.catalog.domain.port.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {
	private final CatalogRepository catalogRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	@KafkaListener(topics = "${kafka.topic.catalog}")
	public void updateQty(String kafkaMessage) {
		log.info("Received message: {}", kafkaMessage);

		Map<Object, Object> map = new HashMap<>();
		try{
			map= objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
		} catch (JsonProcessingException e) {
			log.error("Error while parsing message: {}", e.getMessage());
			throw new RuntimeException(e);
		}

		String productId = map.get("productId").toString();
		Catalog catalog = catalogRepository.findByProductId(productId)
			.orElseThrow();

		catalog.updateStock(catalog.getStock() - Integer.valueOf(map.get("quantity").toString()));
		catalogRepository.save(catalog);	// 이거 이렇게 JPA에서 엔티티를 조회안하고 직접 ID까지 심은 엔티티를 만들어서 save 해도 update되는지 확인 필요
	}

}

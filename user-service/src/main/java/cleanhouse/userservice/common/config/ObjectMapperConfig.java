package cleanhouse.userservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfig {
	@Bean("redisObjectMapper")
	public ObjectMapper redisObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		setLocalDateTimeModule(objectMapper);
		setDeserializationConfig(objectMapper);
		setJsonVisibility(objectMapper);

		return objectMapper;
	}

	private static ObjectMapper setJsonVisibility(ObjectMapper objectMapper) {
		// ⭐ 모든 필드 접근 허용
		return objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	}

	private static void setLocalDateTimeModule(ObjectMapper objectMapper) {
		objectMapper.registerModule(new JavaTimeModule());	// ISO 8601로 직렬화
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);	// LocalDateTime을 timestamp 형식으로 직렬화 안함
	}

	private static void setDeserializationConfig(ObjectMapper objectMapper) {
		// 알 수 없는 속성이 있어도 무시 (역직렬화 시 유연성 - Redis에서 필요)
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


}

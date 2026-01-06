package cleanhouse.apigatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 필터 등록.
 * 어떤 micro service에 붙일지는 .yml에서 적용
 */
@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
	public LoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			// Pre filter
			log.info("Logging filter baseMessage: {}, {}", config.getBaseMessage(), request.getRemoteAddress());
			if (config.isPreLogger()) {
				log.info("Logging filter start: request uri - > {}", request.getURI());
			}

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// Post Filter
				if (config.isPostLogger()) {
					log.info("Logging filter end: response code - > {}", response.getStatusCode());
				}
			}));
		};
	}

	@Data
	public static class Config {
		String baseMessage;
		boolean preLogger;
		boolean postLogger;
	}

}



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
 * 글로벌 필터 등록
 * API Gateway에 등록된 모든 Micro service에 적
 */
@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
	public GlobalFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			// pre filter
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();
			log.info("Global filter message: {}, {}", config.getBaseMessage(), request.getRemoteAddress());

			if (config.isPreLogger()) {
				log.info("Global filter start: request id - > {}", request.getId());
			}
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// post filter
				if (config.isPostLogger()) {
					log.info("Global filter end: response code - > {}", response.getStatusCode());
				}
			}));
		});
	}

	@Data
	public static class Config {
		String baseMessage;
		boolean preLogger;
		boolean postLogger;
	}
}

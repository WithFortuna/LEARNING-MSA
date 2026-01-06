/*
package cleanhouse.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FilterConfig {
	private Environment environment;

	public FilterConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
			.route(r -> r
				.path("/first-service/**")
				.filters(f -> f
					.addRequestHeader("filter-add-request", "1st-request-by-java")
					.addResponseHeader("filter-add-response", "1st-response-header-from-java"))
				.uri("http://localhost:8081")
			)
			.route(r->r
				.path("/second-service/**")
				.filters(f->f
					.addRequestHeader("filter-add-request", "2nd-request-by-java")
					.addResponseHeader("filter-add-response", "2nd-response-header-from-java"))
				.uri("http://localhost:8082")
			)
			.build();
	}

}
*/

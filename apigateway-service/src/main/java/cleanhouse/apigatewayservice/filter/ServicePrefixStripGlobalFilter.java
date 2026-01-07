package cleanhouse.apigatewayservice.filter;

import java.util.regex.Pattern;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


/**
 * AbstractGatewayFilterFactory가 아니라 GlobalFilter를 바로 구현하면 .yml에 안써도 적용됨
 */
@Component
public class ServicePrefixStripGlobalFilter extends AbstractGatewayFilterFactory<ServicePrefixStripGlobalFilter.Config> {
	private static final Pattern HAVE_ONE_SLASH = Pattern.compile("^/[^/]*$");	// ex) /, /user-service -> 이거는 /로 바껴야함
	private static final Pattern RESOURCE = Pattern.compile("^/[^/]+/.*");	// ex) /user-service/ , /user-service/hello

	public ServicePrefixStripGlobalFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) ->{
			var request = exchange.getRequest();
			var path = request.getURI().getPath();

			String newPath = null;
			// /xxx
			if (HAVE_ONE_SLASH.matcher(path).matches()) {
				newPath = path.replace(path, "/");
			}

			// /xxx/yyy → prefix 제거
			if (RESOURCE.matcher(path).matches()) {
				newPath = path.replaceFirst("^/[^/]+", "");
			}

			var mutatedRequest = request.mutate()
				.path(newPath == null ? path : newPath)
				.build();

			return chain.filter(
				exchange.mutate()
					.request(mutatedRequest)
					.build()
			);
		};
	}
	public static class Config {
	}
}

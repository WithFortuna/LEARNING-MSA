package cleanhouse.userservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.TraceContext;
import lombok.RequiredArgsConstructor;

/**
 * Feign Client 요청 시 trace context를 HTTP 헤더로 전파하기 위한 설정
 */
@Configuration
@RequiredArgsConstructor
public class FeignTracingConfig {

	private final Tracer tracer;

	@Bean
	public RequestInterceptor tracingRequestInterceptor() {
		return requestTemplate -> {
			Span currentSpan = tracer.currentSpan();
			if (currentSpan != null) {
				TraceContext context = currentSpan.context();

				// B3 propagation headers
				requestTemplate.header("X-B3-TraceId", context.traceId());
				requestTemplate.header("X-B3-SpanId", context.spanId());
				String parentId = context.parentId();
				if (parentId != null) {
					requestTemplate.header("X-B3-ParentSpanId", parentId);
				}
				requestTemplate.header("X-B3-Sampled", "1");
			}
		};
	}
}

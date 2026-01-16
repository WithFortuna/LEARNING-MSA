package cleanhouse.userservice.security.config;

import static org.springframework.http.HttpMethod.*;

import cleanhouse.userservice.security.infrastructure.authentication.CustomAuthenticationFailureHandler;
import cleanhouse.userservice.security.infrastructure.authentication.CustomAuthenticationSuccessHandler;
import cleanhouse.userservice.security.infrastructure.authentication.CustomLogoutHandler;
import cleanhouse.userservice.security.infrastructure.authentication.CustomLogoutSuccessHandler;
import cleanhouse.userservice.security.infrastructure.authentication.JsonUsernamePasswordAuthenticationFilter;
import cleanhouse.userservice.security.infrastructure.jwt.JwtAuthenticationFilter;
import cleanhouse.userservice.security.infrastructure.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomUserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final PasswordEncoder passwordEncoder;
	private final CustomAuthenticationSuccessHandler successHandler;
	private final CustomAuthenticationFailureHandler failureHandler;
	private final CustomLogoutHandler logoutHandler;
	private final CustomLogoutSuccessHandler logoutSuccessHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
		AuthenticationManager authenticationManager) throws Exception {
		JsonUsernamePasswordAuthenticationFilter loginFilter = new JsonUsernamePasswordAuthenticationFilter(
			authenticationManager);
		loginFilter.setAuthenticationSuccessHandler(successHandler);
		loginFilter.setAuthenticationFailureHandler(failureHandler);

		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
						"/health-check",
						"/welcome",
						"/actuator/**"
					).permitAll()
					.requestMatchers(POST,
						"/users",
						"/auth/login",
						"/auth/refresh"
					).permitAll()
					.requestMatchers(GET,
						"/users/list",
						"/users/me"
					).authenticated()
					.anyRequest().authenticated()
               /* /// Spring security에서 ip 제어를 통해 API gateway 에서 오는 것만 수용하는 도 가능하다만,
                /// 그거 보다는 infra 수준 격리가 더 낫다
                .requestMatchers("/**").access(new WebExpressionAuthorizationManager(
                    "hasIpAddress('127.0.0.1') or hasIpAddress('::1') or hasIpAddress('192.168.0.35')"
                ))*/
			)
			.logout(logout -> logout
				.logoutUrl("/auth/logout")
				.addLogoutHandler(logoutHandler)
				.logoutSuccessHandler(logoutSuccessHandler)
				.permitAll()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	/**
	 * @param config: AuthenticationProvider 빈을 멤버변수 List 로 가짐
	 * @return 등록된 AuthenticationConfiguration에서 등록된 AuthenticationProvider를 가지고 AuthenticationManager 구현체를 만든다
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}

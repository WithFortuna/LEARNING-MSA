package cleanhouse.userservice.user.domain.application.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cleanhouse.userservice.common.exception.CustomException;
import cleanhouse.userservice.common.exception.ErrorCode;
import cleanhouse.userservice.user.adapter.out.client.OrderServiceClient;
import cleanhouse.userservice.user.domain.application.dto.GetCurrentUserQuery;
import cleanhouse.userservice.user.domain.application.dto.GetUsersQuery;
import cleanhouse.userservice.user.domain.application.dto.OrderListResponse;
import cleanhouse.userservice.user.domain.application.dto.UserListResponse;
import cleanhouse.userservice.user.domain.application.dto.UserResponse;
import cleanhouse.userservice.user.domain.application.port.in.UserLoadUsecase;
import cleanhouse.userservice.user.domain.application.port.out.UserRepository;
import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResilienceUserLoadService implements UserLoadUsecase {
    private final UserRepository userRepository;
    // @Qualifier("orderServiceRestTemplateClient")
    @Qualifier("orderServiceFeignClient")
    private final OrderServiceClient orderServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserListResponse getUsers(GetUsersQuery query) {
        Page<User> userPage = userRepository.findAll(query.getPage(), query.getSize());

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());

        return new UserListResponse(
                userResponses,
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getNumber(),
                userPage.getSize()
        );
    }

    @Override
    public UserResponse getCurrentUser(GetCurrentUserQuery query) {
        User user = userRepository.findByEmail(query.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + query.getEmail()));

        UserResponse response = UserResponse.from(user);

        OrderListResponse orderListResponse = null;

        // circuit breaker 처리
        log.info("[user-service] ------------------  order micro service 호출하기 전 ");
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        orderListResponse = circuitbreaker.run(
            () -> orderServiceClient.getOrders(response.getUserId()),
            throwable -> {
                throw new CustomException(ErrorCode.FIND_ORDER_NOT_AVAILABLE);
            });
        log.info("[user-service] ------------------  order micro service 호출한 후 ");

        if (orderListResponse != null && orderListResponse.getOrders() != null) {
            response.setOrders(orderListResponse.getOrders());
        }

        return response;
    }
}

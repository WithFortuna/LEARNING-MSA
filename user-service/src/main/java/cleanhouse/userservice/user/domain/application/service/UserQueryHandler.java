package cleanhouse.userservice.user.domain.application.service;

import cleanhouse.userservice.user.adapter.out.client.OrderServiceClient;
import cleanhouse.userservice.user.adapter.out.client.OrderServiceRestTemplateClient;
import cleanhouse.userservice.user.domain.application.dto.OrderListResponse;
import cleanhouse.userservice.user.domain.application.dto.UserListResponse;
import cleanhouse.userservice.user.domain.application.dto.UserResponse;
import cleanhouse.userservice.user.domain.application.port.in.UserQueryUsecase;
import cleanhouse.userservice.user.domain.application.dto.GetCurrentUserQuery;
import cleanhouse.userservice.user.domain.application.dto.GetUsersQuery;
import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.exception.UserNotFoundException;
import cleanhouse.userservice.user.domain.application.port.out.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserQueryHandler implements UserQueryUsecase {
    private final UserRepository userRepository;
    // @Qualifier("orderServiceRestTemplateClient")
    @Qualifier("orderServiceFeignClient")
    private final OrderServiceClient orderServiceClient;

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
        // try {
            orderListResponse = orderServiceClient.getOrders(response.getUserId());
        // } catch (FeignException exception) {
        //     log.error(exception.getMessage(), exception);
        // }


        if (orderListResponse != null && orderListResponse.getOrders() != null) {
            response.setOrders(orderListResponse.getOrders());
        }

        return response;
    }
}

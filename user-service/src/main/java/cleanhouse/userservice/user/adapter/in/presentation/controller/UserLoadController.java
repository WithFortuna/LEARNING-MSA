package cleanhouse.userservice.user.adapter.in.presentation.controller;

import cleanhouse.userservice.security.infrastructure.userdetails.CustomUserDetails;
import cleanhouse.userservice.user.adapter.out.client.OrderServiceRestTemplateClient;
import cleanhouse.userservice.user.domain.application.dto.UserListResponse;
import cleanhouse.userservice.user.domain.application.dto.UserResponse;
import cleanhouse.userservice.user.domain.application.port.in.UserLoadUsecase;
import cleanhouse.userservice.user.domain.application.dto.GetCurrentUserQuery;
import cleanhouse.userservice.user.domain.application.dto.GetUsersQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserLoadController {
    private final UserLoadUsecase userLoadUsecase;
    private final OrderServiceRestTemplateClient orderServiceClient;

    public UserLoadController(
        @Qualifier("resilienceUserLoadService") UserLoadUsecase userLoadUsecase,
        OrderServiceRestTemplateClient orderServiceClient
    ) {
        this.userLoadUsecase = userLoadUsecase;
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/users/list")
    public ResponseEntity<UserListResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting user list - page: {}, size: {}", page, size);

        GetUsersQuery query = GetUsersQuery.from(page, size);
        UserListResponse response = userLoadUsecase.getUsers(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        log.info("Getting current user info - email: {}", email);

        GetCurrentUserQuery query = GetCurrentUserQuery.from(email);
        UserResponse response = userLoadUsecase.getCurrentUser(query);

        return ResponseEntity.ok(response);
    }
}

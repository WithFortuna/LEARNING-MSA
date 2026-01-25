package cleanhouse.userservice.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	FIND_ORDER_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "일시적으로 주문 조회가 불가능합니다. 잠시만 기다려주세요")
	;

	private final HttpStatus httpStatus;
	private final String message;

}

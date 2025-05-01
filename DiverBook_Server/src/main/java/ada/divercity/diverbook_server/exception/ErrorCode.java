package ada.divercity.diverbook_server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400: 잘못된 요청
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "파라미터 타입이 잘못되었습니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),

    // 401: 인증 실패
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    // 403: 권한 없음
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404: 자원 없음
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 패스워드가 등록되어 있지 않습니다."),
    BADGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 뱃지를 찾을 수 없습니다."),

    // 409: 충돌
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자 이름입니다."),
    USER_ALREADY_ACTIVATED(HttpStatus.CONFLICT, "이미 활성화 된 사용자입니다."),
    PASSWORD_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 사용자의 패스워드가 등록되어 있습니다."),
    DUPLICATE_COLLECTION(HttpStatus.CONFLICT, "이미 도감에 등록된 사용자입니다."),
    DUPLICATE_USER_BADGE(HttpStatus.CONFLICT, "이미 획득한 뱃지입니다."),

    // 422: 처리 불가능
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "요청을 처리할 수 없습니다."),

    // 500: 서버 내부 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "입출력 오류가 발생했습니다."),

    // 기타 커스텀 에러
    BUSINESS_LOGIC_ERROR(HttpStatus.BAD_REQUEST, "비즈니스 로직 처리 중 오류가 발생했습니다.");

    // 필드 및 생성자
    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

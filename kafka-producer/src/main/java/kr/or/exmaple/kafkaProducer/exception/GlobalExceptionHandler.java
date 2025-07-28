package kr.or.exmaple.kafkaProducer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기 (Global Exception Handler)
 * 
 * 이 클래스는 애플리케이션 전체에서 발생하는 예외를 중앙에서 처리합니다.
 * @RestControllerAdvice 어노테이션을 통해 모든 컨트롤러에서 발생하는
 * 예외를 캐치하고 적절한 HTTP 응답으로 변환합니다.
 * 
 * 처리하는 예외 유형:
 * 1. HttpMessageNotReadableException - JSON 파싱 오류
 * 2. Exception - 기타 예상치 못한 모든 예외
 * 
 * 이를 통해 일관된 에러 응답 형식을 제공하고 로깅을 통해
 * 문제 추적을 용이하게 합니다.
 * 
 * @author Spring Boot Kafka Demo
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * JSON 파싱 오류 처리
     * 
     * 클라이언트에서 잘못된 형식의 JSON을 전송했을 때 발생하는
     * HttpMessageNotReadableException을 처리합니다.
     * 
     * 발생 상황:
     * - 잘못된 JSON 구문 (예: {name: "test"} - 따옴표 누락)
     * - 중괄호 불일치
     * - 잘못된 데이터 타입
     * 
     * @param e HttpMessageNotReadableException 예외 객체
     * @return ResponseEntity<String> 400 Bad Request와 에러 메시지
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException e) {
        log.error("JSON parsing error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid JSON format: " + e.getMostSpecificCause().getMessage());
    }

    /**
     * 일반적인 예외 처리
     * 
     * 위에서 처리되지 않은 모든 예외를 캐치하여 처리합니다.
     * 예상치 못한 서버 오류에 대해 500 Internal Server Error를 반환합니다.
     * 
     * 발생 상황:
     * - NullPointerException
     * - IllegalArgumentException
     * - 데이터베이스 연결 오류
     * - 기타 런타임 예외
     * 
     * @param e Exception 예외 객체
     * @return ResponseEntity<String> 500 Internal Server Error와 에러 메시지
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
    }
}
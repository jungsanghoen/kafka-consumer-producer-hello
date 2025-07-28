package kr.or.exmaple.kafkaConsume.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 레코드별 메시지 수신 처리 서비스
 * 
 * 이 서비스는 Kafka Record Listener로부터 전달받은 개별 메시지를
 * 처리하는 비즈니스 로직을 담당합니다.
 * 
 * 주요 기능:
 * - String 형태의 개별 메시지 처리
 * - JSON 파싱 및 구조화된 로그 출력
 * - JSON 파싱 실패 시 에러 로깅
 * - 실시간 메시지 처리
 * 
 * 레코드별 처리의 특징:
 * - 메시지 도착 즉시 개별 처리
 * - 낮은 지연시간 (Low Latency)
 * - 각 메시지가 독립적으로 처리됨
 * - 스트리밍 처리에 최적화
 * 
 * 배치 처리와의 비교:
 * - 배치: 여러 메시지 일괄 처리 → 높은 처리량, 높은 지연시간
 * - 레코드: 개별 메시지 즉시 처리 → 낮은 지연시간, 실시간성
 * 
 * @author Spring Boot Kafka Demo
 */
@Service
@Slf4j
public class MessageReceiveService {

    /**
     * Jackson ObjectMapper - JSON 파싱용
     * 문자열로 전달받은 메시지를 JsonNode 객체로 변환
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 개별 메시지 처리
     * 
     * Kafka Record Listener로부터 전달받은 개별 메시지를 처리합니다.
     * 메시지를 JSON으로 파싱하여 구조화된 로그로 출력합니다.
     * 
     * 레코드별 처리의 장점:
     * - 실시간성: 메시지 도착 즉시 처리
     * - 독립성: 각 메시지가 독립적으로 처리되어 하나의 실패가 다른 메시지에 영향 없음
     * - 낮은 지연시간: 배치를 기다리지 않고 즉시 처리
     * - 스트리밍: 연속적인 데이터 스트림 처리에 적합
     * 
     * 처리 과정:
     * 1. 개별 메시지 수신
     * 2. JSON 파싱 시도
     * 3. 성공: 구조화된 JSON 로그 출력
     * 4. 실패: 에러 로그 출력
     * 
     * @param message Kafka에서 수신한 개별 메시지 (JSON 문자열)
     */
    public void processMessage(String message) {
        try {
            // JSON 문자열을 JsonNode 객체로 파싱
            JsonNode jsonNode = objectMapper.readTree(message);
            
            // 파싱된 JSON을 구조화된 형태로 로깅
            // logback-spring.xml 설정에 의해 JSON 형태로 출력됨
            log.info("Processed single message: {}", jsonNode.toString());
            
        } catch (Exception e) {
            // JSON 파싱 실패 시 에러 로깅
            // 레코드별 처리에서는 개별 메시지 실패가 전체 처리를 중단시키지 않음
            log.error("Failed to parse JSON message: {}", message, e);
        }
    }
}
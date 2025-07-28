package kr.or.exmaple.kafkaConsume.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 배치 메시지 수신 처리 서비스
 * 
 * 이 서비스는 Kafka Batch Listener로부터 전달받은 메시지 리스트를
 * 처리하는 비즈니스 로직을 담당합니다.
 * 
 * 주요 기능:
 * - List<String> 형태의 배치 메시지 처리
 * - 각 메시지를 JSON으로 파싱
 * - 파싱된 JSON 데이터를 구조화된 로그로 출력
 * - JSON 파싱 실패 시 에러 로깅
 * 
 * 처리 과정:
 * 1. 배치 크기 로깅
 * 2. 각 메시지를 순차적으로 처리
 * 3. JSON 파싱 시도
 * 4. 성공: 구조화된 JSON 로그 출력
 * 5. 실패: 에러 로그 출력 및 다음 메시지 처리 계속
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
     * 배치 메시지 리스트 처리
     * 
     * Kafka Batch Listener로부터 전달받은 메시지 리스트를 처리합니다.
     * 각 메시지를 개별적으로 JSON 파싱하여 로깅하며,
     * 하나의 메시지 파싱에 실패해도 전체 배치 처리는 계속됩니다.
     * 
     * 배치 처리의 특징:
     * - 여러 메시지를 한 번에 처리하여 효율성 증대
     * - 개별 메시지 처리 실패가 전체 배치에 영향을 주지 않음
     * - 트랜잭션 처리 시 일관성 보장에 유리
     * 
     * @param messages Kafka에서 수신한 메시지 리스트 (JSON 문자열들)
     */
    public void processMessages(List<String> messages) {
        log.info("Processing batch of {} messages", messages.size());
        
        // 배치 내 각 메시지를 순차 처리
        for (String message : messages) {
            try {
                // JSON 문자열을 JsonNode 객체로 파싱
                JsonNode jsonNode = objectMapper.readTree(message);
                
                // 파싱된 JSON을 구조화된 형태로 로깅
                // logback-spring.xml 설정에 의해 JSON 형태로 출력됨
                log.info("Processed message: {}", jsonNode.toString());
                
            } catch (Exception e) {
                // JSON 파싱 실패 시에도 처리를 중단하지 않고 에러 로깅 후 계속 진행
                // 이는 배치 처리의 장점 중 하나로 일부 메시지 오류가 전체 배치를 중단시키지 않음
                log.error("Failed to parse JSON message: {}", message, e);
            }
        }
        
        // 배치 처리 완료 로깅
        log.info("Completed processing batch of {} messages", messages.size());
    }
}
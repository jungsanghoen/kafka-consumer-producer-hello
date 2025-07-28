package kr.or.exmaple.kafkaProducer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.exmaple.kafkaProducer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * Kafka Producer REST API 컨트롤러
 * 
 * 이 컨트롤러는 HTTP POST 요청을 받아서 JSON 데이터를 처리하고
 * Kafka 토픽으로 메시지를 전송하는 REST API를 제공합니다.
 * 
 * 제공하는 엔드포인트:
 * 1. POST /putdata - Key 없이 메시지 전송
 * 2. POST /putdata-with-key - Key와 함께 메시지 전송
 * 
 * 공통 기능:
 * - JSON 유효성 검증 (Spring Boot 자동 처리)
 * - api_tran_id UUID 자동 생성 및 추가
 * - 두 토픽 모두에 동시 전송
 * - JSON 형태 로깅
 * - 에러 처리 및 적절한 HTTP 상태 코드 반환
 * 
 * @author Spring Boot Kafka Demo
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProducerController {

    /**
     * Kafka 메시지 전송을 담당하는 서비스
     * 생성자 주입을 통해 의존성 주입됨
     */
    private final KafkaProducerService kafkaProducerService;
    
    /**
     * Jackson ObjectMapper - JSON 직렬화/역직렬화 담당
     * Map 객체를 JSON 문자열로 변환할 때 사용
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JSON 데이터를 Key 없이 Kafka로 전송하는 REST API 엔드포인트
     * 
     * 이 메서드는 HTTP POST 요청으로 전달된 JSON 데이터를 받아서
     * 자동으로 api_tran_id를 생성하여 추가한 후 Kafka의 두 토픽
     * (sample.batch.topic, sample.record.topic)으로 전송합니다.
     * 
     * Key가 없으므로 Kafka는 라운드 로빈 방식으로 파티션을 선택합니다.
     * 
     * 요청 예시:
     * POST /putdata
     * Content-Type: application/json
     * {"name": "김철수", "age": 30}
     * 
     * 응답 예시:
     * "Data sent successfully with api_tran_id: 550e8400-e29b-41d4-a716-446655440000"
     * 
     * @param jsonData 클라이언트에서 전송한 JSON 데이터 (Map으로 자동 변환)
     * @return ResponseEntity<String> 처리 결과 메시지와 HTTP 상태 코드
     */
    @PostMapping("/putdata")
    public ResponseEntity<String> putData(@RequestBody Map<String, Object> jsonData) {
        log.info("Received data: {}", jsonData);
        
        // 입력 데이터 유효성 검증
        if (jsonData == null || jsonData.isEmpty()) {
            log.warn("Received empty JSON data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON data is required");
        }
        
        try {
            // 고유한 API 트랜잭션 ID 생성 및 추가
            String apiTranId = UUID.randomUUID().toString();
            jsonData.put("api_tran_id", apiTranId);
            
            // Map 객체를 JSON 문자열로 직렬화
            String enhancedJsonData = objectMapper.writeValueAsString(jsonData);
            log.info("Enhanced data with api_tran_id: {}", enhancedJsonData);
            
            // 두 개의 Kafka 토픽 모두에 메시지 전송 (Key 없음)
            kafkaProducerService.sendToBatchTopic(enhancedJsonData);
            kafkaProducerService.sendToRecordTopic(enhancedJsonData);
            
            return ResponseEntity.ok("Data sent successfully with api_tran_id: " + apiTranId);
            
        } catch (Exception e) {
            // JSON 직렬화 오류 또는 Kafka 전송 오류 처리
            log.error("Error processing data: {}", jsonData, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing data: " + e.getMessage());
        }
    }

    /**
     * JSON 데이터를 Key와 함께 Kafka로 전송하는 REST API 엔드포인트
     * 
     * 이 메서드는 HTTP POST 요청으로 전달된 JSON 데이터에서 "key" 필드를
     * 추출하여 Kafka 메시지 키로 사용하고, api_tran_id를 생성하여 추가한 후
     * Kafka의 두 토픽(sample.batch.topic, sample.record.topic)으로 전송합니다.
     * 
     * Key가 있으므로 Kafka는 해시 함수를 사용하여 동일한 Key를 가진
     * 메시지들을 같은 파티션으로 전송합니다. 이를 통해 메시지 순서 보장과
     * 파티션별 그룹 처리가 가능합니다.
     * 
     * 요청 예시:
     * POST /putdata-with-key
     * Content-Type: application/json
     * {"key": "user123", "name": "김철수", "age": 30}
     * 
     * 응답 예시:
     * "Data sent successfully with key: user123 and api_tran_id: 550e8400-e29b-41d4-a716-446655440000"
     * 
     * @param jsonData 클라이언트에서 전송한 JSON 데이터 (반드시 "key" 필드 포함)
     * @return ResponseEntity<String> 처리 결과 메시지와 HTTP 상태 코드
     */
    @PostMapping("/putdata-with-key")
    public ResponseEntity<String> putDataWithKey(@RequestBody Map<String, Object> jsonData) {
        log.info("Received data with key requirement: {}", jsonData);
        
        // 입력 데이터 유효성 검증
        if (jsonData == null || jsonData.isEmpty()) {
            log.warn("Received empty JSON data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON data is required");
        }
        
        // "key" 필드 존재 여부 및 null 값 검증
        // Kafka 메시지 키로 사용될 필수 필드임
        if (!jsonData.containsKey("key") || jsonData.get("key") == null) {
            log.warn("Missing required 'key' field in JSON data: {}", jsonData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required 'key' field in JSON data");
        }
        
        try {
            // JSON에서 key 값 추출 및 문자열 변환
            String key = jsonData.get("key").toString();
            
            // 고유한 API 트랜잭션 ID 생성 및 추가
            String apiTranId = UUID.randomUUID().toString();
            jsonData.put("api_tran_id", apiTranId);
            
            // Map 객체를 JSON 문자열로 직렬화
            String enhancedJsonData = objectMapper.writeValueAsString(jsonData);
            log.info("Enhanced data with api_tran_id and key {}: {}", key, enhancedJsonData);
            
            // 두 개의 Kafka 토픽 모두에 Key와 함께 메시지 전송
            // 동일한 Key를 가진 메시지들은 같은 파티션으로 전송됨
            kafkaProducerService.sendToBatchTopic(key, enhancedJsonData);
            kafkaProducerService.sendToRecordTopic(key, enhancedJsonData);
            
            return ResponseEntity.ok("Data sent successfully with key: " + key + " and api_tran_id: " + apiTranId);
            
        } catch (Exception e) {
            // JSON 직렬화 오류 또는 Kafka 전송 오류 처리
            log.error("Error processing data with key: {}", jsonData, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing data: " + e.getMessage());
        }
    }
}
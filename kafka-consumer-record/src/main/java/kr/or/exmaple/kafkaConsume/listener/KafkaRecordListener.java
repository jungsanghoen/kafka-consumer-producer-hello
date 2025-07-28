package kr.or.exmaple.kafkaConsume.listener;

import kr.or.exmaple.kafkaConsume.service.MessageReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka 레코드별 메시지 리스너
 * 
 * 이 클래스는 Apache Kafka의 sample.record.topic에서 메시지를
 * 레코드별(Record-by-Record) 형태로 수신하는 리스너입니다.
 * 
 * Spring Kafka의 @KafkaListener 어노테이션을 사용하여
 * 지정된 토픽을 구독하고, 메시지가 도착하면 자동으로
 * listen() 메서드가 호출됩니다.
 * 
 * 레코드 리스너의 특징:
 * - 기본 리스너 모드 (application.yml에 listener.type 설정 없음)
 * - String 형태로 개별 메시지를 하나씩 수신
 * - 실시간 처리: 메시지 도착 즉시 처리
 * - 낮은 지연시간 (Low Latency)
 * - 스트리밍 처리에 최적화
 * 
 * 배치 리스너와의 차이점:
 * - 배치: List<String> vs 레코드: String
 * - 배치: 여러 메시지 일괄 처리 vs 레코드: 개별 메시지 즉시 처리
 * - 배치: 높은 처리량 vs 레코드: 실시간성
 * 
 * 설정 정보:
 * - 토픽: sample.record.topic
 * - Consumer Group: sample-consumer
 * - 처리 방식: 개별 메시지 즉시 처리
 * 
 * @author Spring Boot Kafka Demo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRecordListener {

    /**
     * 메시지 처리 서비스
     * 생성자 주입을 통해 의존성 주입됨
     */
    private final MessageReceiveService messageReceiveService;

    /**
     * Kafka 레코드별 메시지 수신 리스너 메서드
     * 
     * @KafkaListener 어노테이션에 의해 sample.record.topic을 구독하며,
     * 메시지가 도착하면 Spring Kafka가 자동으로 이 메서드를 호출합니다.
     * 
     * 레코드별 처리 과정:
     * 1. Kafka에서 개별 메시지 수신 (String 형태)
     * 2. 메시지 수신을 로깅
     * 3. MessageReceiveService에 개별 메시지 처리 위임
     * 4. 처리 완료 즉시 다음 메시지 대기
     * 
     * 실시간 처리의 장점:
     * - 즉시성: 메시지 도착 즉시 처리로 낮은 지연시간
     * - 독립성: 각 메시지가 독립적으로 처리됨
     * - 연속성: 스트리밍 데이터 처리에 적합
     * - 장애 격리: 하나의 메시지 처리 실패가 다른 메시지에 영향 없음
     * 
     * 주의사항:
     * - Consumer offset은 개별 메시지 처리 완료 후 커밋됨
     * - 예외 발생 시 Spring Kafka의 에러 핸들링 정책에 따라 처리됨
     * - 높은 처리량이 필요한 경우 배치 처리 방식을 고려해야 함
     * 
     * @param message Kafka에서 수신한 개별 메시지 (JSON 문자열)
     */
    @KafkaListener(topics = "sample.record.topic", groupId = "sample-consumer")
    public void listen(String message) {
        // 개별 메시지 수신 로깅 - 처리 시작을 알림
        log.info("Received single message from topic: sample.record.topic");
        
        // 실제 비즈니스 로직은 서비스 레이어에 위임
        // 관심사 분리(Separation of Concerns) 원칙 적용
        messageReceiveService.processMessage(message);
        
        // 개별 메시지 처리 완료 로깅
        log.info("Completed processing single message from sample.record.topic");
    }
}
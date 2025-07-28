package kr.or.exmaple.kafkaConsume.listener;

import kr.or.exmaple.kafkaConsume.service.MessageReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka 배치 메시지 리스너
 * 
 * 이 클래스는 Apache Kafka의 sample.batch.topic에서 메시지를
 * 배치(Batch) 형태로 수신하는 리스너입니다.
 * 
 * Spring Kafka의 @KafkaListener 어노테이션을 사용하여
 * 지정된 토픽을 구독하고, 메시지가 도착하면 자동으로
 * listen() 메서드가 호출됩니다.
 * 
 * 배치 리스너의 특징:
 * - application.yml의 listener.type: batch 설정에 의해 활성화
 * - max.poll.records 설정에 따라 한 번에 받는 메시지 수 결정
 * - List<String> 형태로 여러 메시지를 한 번에 수신
 * - 네트워크 오버헤드 감소 및 처리 효율성 증대
 * - 트랜잭션 처리 시 일관성 보장에 유리
 * 
 * 설정 정보:
 * - 토픽: sample.batch.topic
 * - Consumer Group: sample-consumer
 * - 최대 배치 크기: 10개 (max.poll.records)
 * 
 * @author Spring Boot Kafka Demo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaBatchListener {

    /**
     * 메시지 처리 서비스
     * 생성자 주입을 통해 의존성 주입됨
     */
    private final MessageReceiveService messageReceiveService;

    /**
     * Kafka 배치 메시지 수신 리스너 메서드
     * 
     * @KafkaListener 어노테이션에 의해 sample.batch.topic을 구독하며,
     * 메시지가 도착하면 Spring Kafka가 자동으로 이 메서드를 호출합니다.
     * 
     * 배치 처리 과정:
     * 1. Kafka에서 최대 max.poll.records 개수만큼 메시지 수신
     * 2. List<String> 형태로 여러 메시지를 한 번에 전달받음
     * 3. 수신한 배치 크기를 로깅
     * 4. MessageReceiveService에 배치 처리 위임
     * 
     * 주의사항:
     * - 배치 내 하나의 메시지 처리 실패가 전체 배치를 실패시키지 않음
     * - Consumer offset은 배치 전체 처리 완료 후 커밋됨
     * - 예외 발생 시 Spring Kafka의 에러 핸들링 정책에 따라 처리됨
     * 
     * @param messages Kafka에서 수신한 메시지 리스트 (JSON 문자열 배치)
     */
    @KafkaListener(topics = "sample.batch.topic", groupId = "sample-consumer")
    public void listen(List<String> messages) {
        // 배치 수신 로깅 - 처리 시작을 알림
        log.info("Received batch of {} messages from topic: sample.batch.topic", messages.size());
        
        // 실제 비즈니스 로직은 서비스 레이어에 위임
        // 관심사 분리(Separation of Concerns) 원칙 적용
        messageReceiveService.processMessages(messages);
        
        // 배치 처리 완료 로깅
        log.info("Completed processing batch from sample.batch.topic");
    }
}
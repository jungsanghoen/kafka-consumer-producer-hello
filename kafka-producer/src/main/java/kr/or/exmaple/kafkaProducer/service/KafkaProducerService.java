package kr.or.exmaple.kafkaProducer.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka 메시지 전송을 담당하는 서비스 클래스
 * 
 * 이 클래스는 Spring Kafka의 KafkaTemplate을 사용하여
 * 두 개의 토픽(sample.batch.topic, sample.record.topic)으로
 * 메시지를 전송하는 기능을 제공합니다.
 * 
 * 지원하는 전송 방식:
 * 1. Key 없는 메시지 전송 - 파티션은 라운드 로빈 방식으로 배정
 * 2. Key 포함 메시지 전송 - 동일한 Key는 같은 파티션으로 전송됨
 * 
 * 토픽 정보:
 * - sample.batch.topic: 배치 컨슈머에서 소비
 * - sample.record.topic: 레코드별 컨슈머에서 소비
 * 
 * @author Spring Boot Kafka Demo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    /**
     * Spring Kafka에서 제공하는 KafkaTemplate
     * Key와 Value 모두 String 타입으로 설정됨
     * application.yml의 kafka.producer 설정을 사용
     */
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    /**
     * Micrometer 메트릭 레지스트리
     * Prometheus 메트릭 수집을 위해 사용
     */
    private final MeterRegistry meterRegistry;

    /**
     * sample.batch.topic으로 Key 없이 메시지 전송
     * 
     * Key가 없는 경우 Kafka는 기본적으로 라운드 로빈 방식으로
     * 파티션을 선택하여 메시지를 분산 저장합니다.
     * 
     * @param message 전송할 JSON 문자열 메시지
     */
    public void sendToBatchTopic(String message) {
        kafkaTemplate.send("sample.batch.topic", message);
        meterRegistry.counter("custom-kafka-produce", "topic", "sample.batch.topic").increment();
        log.info("Message sent to sample.batch.topic: {}", message);
    }

    /**
     * sample.record.topic으로 Key 없이 메시지 전송
     * 
     * Key가 없는 경우 Kafka는 기본적으로 라운드 로빈 방식으로
     * 파티션을 선택하여 메시지를 분산 저장합니다.
     * 
     * @param message 전송할 JSON 문자열 메시지
     */
    public void sendToRecordTopic(String message) {
        kafkaTemplate.send("sample.record.topic", message);
        meterRegistry.counter("custom-kafka-produce", "topic", "sample.record.topic").increment();
        log.info("Message sent to sample.record.topic: {}", message);
    }

    /**
     * sample.batch.topic으로 Key와 함께 메시지 전송 (오버로딩 메서드)
     * 
     * Key가 있는 경우 Kafka는 해시 함수를 사용하여 동일한 Key를
     * 가진 메시지들을 같은 파티션으로 전송합니다.
     * 이를 통해 메시지 순서 보장과 파티션별 처리가 가능합니다.
     * 
     * @param key 메시지 키 (파티션 선택에 사용)
     * @param message 전송할 JSON 문자열 메시지
     */
    public void sendToBatchTopic(String key, String message) {
        kafkaTemplate.send("sample.batch.topic", key, message);
        meterRegistry.counter("custom-kafka-produce", "topic", "sample.batch.topic").increment();
        log.info("Message sent to sample.batch.topic with key {}: {}", key, message);
    }

    /**
     * sample.record.topic으로 Key와 함께 메시지 전송 (오버로딩 메서드)
     * 
     * Key가 있는 경우 Kafka는 해시 함수를 사용하여 동일한 Key를
     * 가진 메시지들을 같은 파티션으로 전송합니다.
     * 이를 통해 메시지 순서 보장과 파티션별 처리가 가능합니다.
     * 
     * @param key 메시지 키 (파티션 선택에 사용)
     * @param message 전송할 JSON 문자열 메시지
     */
    public void sendToRecordTopic(String key, String message) {
        kafkaTemplate.send("sample.record.topic", key, message);
        meterRegistry.counter("custom-kafka-produce", "topic", "sample.record.topic").increment();
        log.info("Message sent to sample.record.topic with key {}: {}", key, message);
    }
}
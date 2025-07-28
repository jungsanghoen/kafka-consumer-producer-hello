package kr.or.exmaple.kafkaConsume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kafka Batch Consumer 애플리케이션의 메인 클래스
 * 
 * 이 애플리케이션은 Apache Kafka의 sample.batch.topic에서 메시지를
 * 배치(Batch) 방식으로 소비하는 컨슈머 애플리케이션입니다.
 * 
 * 주요 특징:
 * - 배치 처리: 여러 메시지를 List로 한 번에 받아서 처리
 * - max.poll.records: 10 설정으로 최대 10개 메시지씩 배치 처리
 * - Consumer Group: sample-consumer
 * - 토픽: sample.batch.topic
 * - JSON 메시지 파싱 및 로깅
 * - JSON 형태 로그 출력 (Console & File)
 * 
 * 배치 처리의 장점:
 * - 처리 효율성 증대 (여러 메시지 일괄 처리)
 * - 네트워크 오버헤드 감소
 * - 트랜잭션 처리에 유리
 * 
 * 포트: 18888
 * Kafka 서버: localhost:9092
 * 
 * @author Spring Boot Kafka Demo
 * @version 1.0
 */
@SpringBootApplication
public class KafkaConsumerBatchApplication {

	/**
	 * 애플리케이션 시작점
	 * Spring Boot 애플리케이션을 시작하고 Kafka Listener를 활성화합니다.
	 * 
	 * @param args 명령행 인수
	 */
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerBatchApplication.class, args);
	}
}

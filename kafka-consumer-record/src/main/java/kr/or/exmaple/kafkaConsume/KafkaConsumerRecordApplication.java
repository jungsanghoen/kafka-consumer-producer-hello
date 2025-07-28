package kr.or.exmaple.kafkaConsume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kafka Record Consumer 애플리케이션의 메인 클래스
 * 
 * 이 애플리케이션은 Apache Kafka의 sample.record.topic에서 메시지를
 * 레코드별(Record-by-Record) 방식으로 소비하는 컨슈머 애플리케이션입니다.
 * 
 * 주요 특징:
 * - 개별 처리: 메시지를 하나씩 개별적으로 받아서 처리
 * - 실시간 처리: 메시지 도착 즉시 개별 처리로 낮은 지연시간
 * - Consumer Group: sample-consumer
 * - 토픽: sample.record.topic
 * - JSON 메시지 파싱 및 로깅
 * - JSON 형태 로그 출력 (Console & File)
 * 
 * 레코드별 처리의 장점:
 * - 실시간성: 메시지 도착 즉시 처리
 * - 낮은 지연시간 (Low Latency)
 * - 개별 메시지 처리 실패가 다른 메시지에 영향 없음
 * - 스트리밍 처리에 적합
 * 
 * 배치 처리와의 차이점:
 * - 배치: List<String> vs 레코드: String
 * - 배치: 높은 처리량, 높은 지연시간 vs 레코드: 낮은 지연시간, 실시간 처리
 * 
 * 포트: 18887 (배치 컨슈머와 다른 포트)
 * Kafka 서버: localhost:9092
 * 
 * @author Spring Boot Kafka Demo
 * @version 1.0
 */
@SpringBootApplication
public class KafkaConsumerRecordApplication {

	/**
	 * 애플리케이션 시작점
	 * Spring Boot 애플리케이션을 시작하고 Kafka Listener를 활성화합니다.
	 * 
	 * @param args 명령행 인수
	 */
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerRecordApplication.class, args);
	}
}

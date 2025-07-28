package kr.or.exmaple.kafkaProducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kafka Producer 애플리케이션의 메인 클래스
 * 
 * 이 애플리케이션은 REST API를 통해 JSON 데이터를 받아서
 * Apache Kafka의 두 개 토픽(sample.batch.topic, sample.record.topic)으로
 * 메시지를 전송하는 역할을 담당합니다.
 * 
 * 주요 기능:
 * - REST API 엔드포인트 제공 (/putdata, /putdata-with-key)
 * - JSON 유효성 검증
 * - 자동 api_tran_id UUID 생성
 * - Kafka 메시지 전송 (key 포함/미포함)
 * - JSON 형태 로깅
 * - Prometheus 메트릭 수집
 * 
 * 포트: 18889
 * Kafka 서버: localhost:9092
 * 
 * @author Spring Boot Kafka Demo
 * @version 1.0
 */
@SpringBootApplication
public class KafkaProducerApplication {

	/**
	 * 애플리케이션 시작점
	 * Spring Boot 애플리케이션을 시작하고 내장된 Tomcat 서버를 구동합니다.
	 * 
	 * @param args 명령행 인수
	 */
	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args);
	}
}

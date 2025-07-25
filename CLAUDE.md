# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure

This is a multi-module Maven project with two Spring Boot applications:

- **KafkaConsumer** - Kafka consumer application (port 18888)
- **KafkaProducer** - Kafka producer application (port 18889)

Both modules are managed by a parent POM with shared dependency management.

## Development Commands

### Build Commands
```bash
# Build entire project
./mvnw clean install

# Build specific module
./mvnw clean install -pl KafkaConsumer
./mvnw clean install -pl KafkaProducer

# Compile only
./mvnw compile
```

### Running Applications
```bash
# Run consumer application
cd KafkaConsumer && ../mvnw spring-boot:run

# Run producer application  
cd KafkaProducer && ../mvnw spring-boot:run
```

### Testing
```bash
# Run all tests
./mvnw test

# Run tests for specific module
./mvnw test -pl KafkaConsumer
./mvnw test -pl KafkaProducer
```

## Technology Stack

- Java 21
- Spring Boot 3.5.3
- Spring Kafka
- Maven 3
- Lombok for boilerplate reduction
- Spring Boot Actuator with Prometheus metrics
- Spring Boot Web for REST endpoints

## Configuration

- Consumer application runs on port 18888
- Producer application runs on port 18889
- Both applications expose all actuator endpoints at `/actuator/*`
- Prometheus metrics available at `/actuator/prometheus`
- Application names are configured in `application.properties`

## Project Notes

- Maven wrapper (`mvnw`) requires JAVA_HOME to be set
- Parent POM manages dependency versions centrally
- Both modules use identical Spring Boot and dependency configurations
- Lombok annotation processing is configured in compiler plugin
- Package structure follows `kr.or.exmaple.*` convention (note: contains typo in "exmaple")
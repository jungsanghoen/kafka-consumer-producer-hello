# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure

This is a multi-module Maven project with three Spring Boot applications:

- **kafka-consumer-batch** - Kafka batch consumer application (port 18888)
- **kafka-consumer-record** - Kafka record-by-record consumer application (port 18888)
- **kafka-producer** - Kafka producer application (port 18889)

All modules are managed by a parent POM with shared dependency management. Both consumer modules demonstrate different Kafka consumption patterns.

## Development Commands

### Build Commands
```bash
# Build entire project
./mvnw clean install

# Build specific module
./mvnw clean install -pl kafka-consumer-batch
./mvnw clean install -pl kafka-consumer-record
./mvnw clean install -pl kafka-producer

# Compile only
./mvnw compile
```

### Running Applications
```bash
# Run batch consumer application
cd kafka-consumer-batch && ../mvnw spring-boot:run

# Run record consumer application
cd kafka-consumer-record && ../mvnw spring-boot:run

# Run producer application  
cd kafka-producer && ../mvnw spring-boot:run
```

### Testing
```bash
# Run all tests
./mvnw test

# Run tests for specific module
./mvnw test -pl kafka-consumer-batch
./mvnw test -pl kafka-consumer-record
./mvnw test -pl kafka-producer
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

- Both consumer applications run on port 18888
- Producer application runs on port 18889  
- Applications expose limited actuator endpoints: `prometheus`, `info`, `health`
- Prometheus metrics available at `/actuator/prometheus`
- Application names are configured in `application.properties`
- Port configurations and actuator settings are in `application.yml`

## Project Notes

- Maven wrapper (`mvnw`) requires JAVA_HOME to be set
- Parent POM manages dependency versions centrally
- All modules use nearly identical Spring Boot and dependency configurations
- Lombok annotation processing is configured in compiler plugin
- Package structure follows `kr.or.exmaple.*` convention (note: contains typo in "exmaple")
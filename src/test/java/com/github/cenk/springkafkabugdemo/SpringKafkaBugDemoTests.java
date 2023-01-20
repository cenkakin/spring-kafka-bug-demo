package com.github.cenk.springkafkabugdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
class SpringKafkaBugDemoTests {

    final static String FIRST_TOPIC = "retry.bug.demo.topic1";
    final static String SECOND_TOPIC = "retry.bug.demo.topic2";

    private final static KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    static {
        KAFKA.start();
    }

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    @Test
    void should_succesfully_configure_retry() {
        Assertions.assertTrue(true);
    }

    static class RetryableKafkaListeners {
        @RetryableTopic()
        @KafkaListener(topics = SpringKafkaBugDemoTests.FIRST_TOPIC)
        void firstListener(String message) {
        }

        @RetryableTopic()
        @KafkaListener(topics = SpringKafkaBugDemoTests.SECOND_TOPIC)
        void secondListener(String message) {
        }
    }

    @TestConfiguration
    static class SpringKafkaBugDemoConfiguration {

        @Bean
        public RetryableKafkaListeners retryableKafkaListeners() {
            return new RetryableKafkaListeners();
        }
    }
}

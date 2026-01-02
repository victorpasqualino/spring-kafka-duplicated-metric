package com.victorpasqualino.kafka.retry;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class KafkaRetryApplicationTests {

    @Container
    static final KafkaContainer kafka = new KafkaContainer("apache/kafka:4.1.1");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    MeterRegistry meterRegistry;

    @Test
    void contextLoads() {

        kafkaTemplate.send("your-topic-name", "key", "test message");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long count = meterRegistry.get("spring.kafka.listener").timer().count();

        assertEquals(1, count);
    }
}

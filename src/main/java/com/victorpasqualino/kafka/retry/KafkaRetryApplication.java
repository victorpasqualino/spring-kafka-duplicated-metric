package com.victorpasqualino.kafka.retry;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class KafkaRetryApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaRetryApplication.class, args);
    }
}

@Component
class ListenTopic {

    @RetryableTopic
    @KafkaListener(id = "your-listener-id", idIsGroup = false, topics = "your-topic-name")
    void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {

        System.out.println("Received message: " + record.value());

        acknowledgment.acknowledge();
    }
}

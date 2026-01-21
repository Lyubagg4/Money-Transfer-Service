package ru.zyryanova.TransferService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.example.TransferCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig{
    @Autowired
    private Environment environment;

    Map<String, Object> producerConfig(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,environment.getProperty("spring.kafka.producer.bootstrap-servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,environment.getProperty("spring.kafka.producer.key-serializer"));
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,environment.getProperty("spring.kafka.producer.value-serializer"));
        config.put(ProducerConfig.ACKS_CONFIG,environment.getProperty("spring.kafka.producer.acks"));
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.producer.properties.delivery.timeout.ms"));
        config.put(ProducerConfig.LINGER_MS_CONFIG,environment.getProperty("spring.kafka.producer.properties.linger.ms"));
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,environment.getProperty("spring.kafka.producer.properties.request.timeout.ms"));
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,environment.getProperty("spring.kafka.producer.properties.enable.idempotence"));
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,environment.getProperty("spring.kafka.producer.properties.max.in.flight.requests.per.connection"));
        return config;
    }

    @Bean
    ProducerFactory<String, String> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    NewTopic createTransferCreatedTopic(){
        return TopicBuilder.name("transfer-created-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas","2"))
                .build();
    }
}

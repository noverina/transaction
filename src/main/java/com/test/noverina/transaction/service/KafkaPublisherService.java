package com.test.noverina.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.noverina.transaction.dto.TransactionMessageDto;
import com.test.noverina.transaction.util.UuidV6Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaPublisherService {
    @Value("${spring.kafka.topic-name}")
    private String topic;
    @Autowired
    private UuidV6Generator generator;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper mapper;

    public KafkaPublisherService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String message) throws JsonProcessingException {
        try {
            var key = generator.generate();
            //this is to ensure the json is a valid transaction class
            mapper.readValue(message, TransactionMessageDto.class);
            kafkaTemplate.send(topic, key, message);
            log.info("Message {} published to topic {}", key, topic);
        } catch (Exception ex) {
            if (ex.getCause() != null)
                log.error("[{} ({})] {}: {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getCause().toString(), ex.getMessage());
            else
                log.error("[{} ({})] {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getMessage());
        }

    }

}

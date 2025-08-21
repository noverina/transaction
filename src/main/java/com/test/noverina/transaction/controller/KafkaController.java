package com.test.noverina.transaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.noverina.transaction.service.KafkaPublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaPublisherService publisher;

    @Operation(summary = "(for demo/test purpose) publish message")
    @PostMapping
    public void publish(@Parameter(description = "Message should be in JSON Format") @RequestParam String message) throws JsonProcessingException {
        publisher.sendMessage(message);
    }
}

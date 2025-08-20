package com.test.noverina.transaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.noverina.transaction.service.KafkaPublisherService;
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

    @PostMapping
    public void publish(@RequestParam String message) throws JsonProcessingException {
        publisher.sendMessage(message);
    }

}

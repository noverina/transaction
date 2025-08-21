package com.test.noverina.transaction.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.noverina.transaction.dto.TransactionMessageDto;
import com.test.noverina.transaction.entity.Transaction;
import com.test.noverina.transaction.enums.TransactionType;
import com.test.noverina.transaction.repository.AccountRepository;
import com.test.noverina.transaction.repository.CurrencyRepository;
import com.test.noverina.transaction.repository.TransactionRepository;
import com.test.noverina.transaction.util.ExchangeRateHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
@Slf4j
public class TransactionConsumerService {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CurrencyRepository currencyRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private TransactionRepository transactionRepo;
    @Autowired
    private ExchangeRateHelper rateHelper;

    @KafkaListener(topics = "#{'${spring.kafka.topic-name}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void consumeMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
       try {
           var key = record.key();
           var message = record.value();
           var topic = record.topic();
           log.info("Received message {} from topic {}", key, topic);

           var dto =  mapper.readValue(message, TransactionMessageDto.class);
           var currency = currencyRepo.findById(dto.getCurrencyId()).orElseThrow();
           var account = accountRepo.findById(dto.getAccountId()).orElseThrow();
           var fromCode = currency.getCurrencyId();
           var toCode = account.getCurrency().getCurrencyId();
           var amount = new BigDecimal(dto.getAmount());
           var entity = new Transaction(key,
                   currency,
                   account,
                   OffsetDateTime.parse(dto.getDate()),
                   dto.getType(),
                   Money.of(amount, fromCode),
                   dto.getDescription());
           transactionRepo.save(entity);
           var initialBalance = account.getBalance();
           var exchangeRate = rateHelper.getRate(fromCode, toCode);
           var convertedAmount = amount.multiply(exchangeRate);
           var convertedMoney = Money.of(convertedAmount, toCode);
           var updatedBalance = dto.getType() == TransactionType.INCOMING ?
                   initialBalance.add(convertedMoney) :
                   initialBalance.subtract(convertedMoney);
           account.setBalance(updatedBalance);
           accountRepo.save(account);
           log.info("Message {} persisted to DB", key);
       } catch (Exception ex) {
           if (ex.getCause() != null)
               log.error("[{} ({})] {}: Failed to persist message | {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getCause().toString(), ex.getMessage());
           else
               log.error("[{} ({})]: Failed to persist message (unknown cause) {}", ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex.getMessage());
           throw ex;
       }
    }
}


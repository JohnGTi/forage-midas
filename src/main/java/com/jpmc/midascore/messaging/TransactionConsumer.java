package com.jpmc.midascore.messaging;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @Autowired
    private TransactionServiceImpl transactionService;

    @KafkaListener(
            topics = "${general.kafka-topic}"
            , groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(ConsumerRecord<String, Transaction> record) {

        // "listen" is concerned with directing control to the appropriate service.

        transactionService.onReceive(record.value());
    }
}

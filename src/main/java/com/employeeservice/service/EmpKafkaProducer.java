package com.employeeservice.service;

import com.employeeservice.model.EmployeeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmpKafkaProducer {

    @Value(value = "${PRODUCER_TOPIC}")
    private String topic;
    private final ReactiveKafkaProducerTemplate<String, EmployeeRequest> reactiveKafkaProducerTemplate;

    public void sendMessages(EmployeeRequest employeeRequest) {
        log.info("send to topic={}, {}={},", topic, EmployeeRequest.class.getSimpleName(), employeeRequest);
        reactiveKafkaProducerTemplate.send(topic, String.valueOf(employeeRequest.getEmployeeId()), employeeRequest)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}",
                        employeeRequest,
                        senderResult.recordMetadata().offset()))
                .subscribe();
    }

}

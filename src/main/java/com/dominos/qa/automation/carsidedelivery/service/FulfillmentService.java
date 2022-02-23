package com.dominos.qa.automation.carsidedelivery.service;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface FulfillmentService {
    Flux<ServerSentEvent<String>> subscribe(String categories);
}

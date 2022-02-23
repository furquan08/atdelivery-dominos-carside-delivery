package com.dominos.qa.automation.carsidedelivery.service.impl;

import com.dominos.qa.automation.carsidedelivery.service.FulfillmentService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class FulfillmentServiceImpl implements FulfillmentService {
    ParameterizedTypeReference<ServerSentEvent<String>> typeRef = new ParameterizedTypeReference<>() {
    };
    @Autowired
    private ApplicationState applicationState;
    @Autowired
    private WebClient fsClient;
    @Value("${store.id}")
    private String storeId;
    private String id;

    public Flux<ServerSentEvent<String>> subscribe(String categories) {

        id = storeId;
        return fsClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stores/")
                        .path(id)
                        .queryParam("categories", categories)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve().bodyToFlux(typeRef);
    }
}

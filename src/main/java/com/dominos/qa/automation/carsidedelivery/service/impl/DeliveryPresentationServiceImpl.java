package com.dominos.qa.automation.carsidedelivery.service.impl;


import com.dominos.ecommerce.dps.domain.event.duc.DucStatusEvent;
import com.dominos.qa.automation.carsidedelivery.service.DeliveryPresentationService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

@Service
public class DeliveryPresentationServiceImpl implements DeliveryPresentationService {

    ParameterizedTypeReference<ServerSentEvent<String>> typeRef = new ParameterizedTypeReference<>() {
    };
    @Autowired
    private ApplicationState applicationState;
    @Autowired
    private WebClient dpsClient;
    @Value("${store.id}")
    private String storeId;

    @Override
    public Flux<ServerSentEvent<String>> subscribe() {

        return dpsClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stores/")
                        .path(storeId)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve().bodyToFlux(typeRef);
    }

    @Override
    public Mono<DucStatusEvent> getDucStatus() {
        return dpsClient.get()
                .uri("/duc/" + applicationState.get("pulseOrderGuid")).accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(DucStatusEvent.class);
    }

    @Override
    public Mono<ClientResponse> setDUCStatus(String statusType) {
        return dpsClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/orders/" + applicationState.get("pulseOrderGuid") + "/status")
                        .queryParam("category", "DRIVE_UP_CARRYOUT_STATUS")
                        .queryParam("value", statusType)
                        .build())
                .exchange();
    }

    @Override
    public Mono<List> getCarsideTeamMembers() {
        return dpsClient.get()
                .uri("/carsideTeamMembers/" + storeId)
                .retrieve()
                .bodyToMono(List.class);
    }

    @Override
    public Mono<ClientResponse> carsideDispatches(Map request) {
        System.out.println(BodyInserters.fromObject(request));
        return dpsClient.post()
                .uri("/orders/" + applicationState.get("pulseOrderGuid") + "/carsideDispatches")
                .accept(MediaType.ALL).contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange();
    }
}

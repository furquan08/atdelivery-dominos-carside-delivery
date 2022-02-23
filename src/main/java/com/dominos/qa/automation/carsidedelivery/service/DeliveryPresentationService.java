package com.dominos.qa.automation.carsidedelivery.service;

import com.dominos.ecommerce.dps.domain.event.duc.DucStatusEvent;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

public interface DeliveryPresentationService {

    Flux<ServerSentEvent<String>> subscribe();

    Mono<DucStatusEvent> getDucStatus();

    Mono<ClientResponse> setDUCStatus(String statusType);

    Mono<List> getCarsideTeamMembers();

    Mono<ClientResponse> carsideDispatches(Map request);
}

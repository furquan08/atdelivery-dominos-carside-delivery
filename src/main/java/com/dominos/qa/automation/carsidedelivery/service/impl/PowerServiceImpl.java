package com.dominos.qa.automation.carsidedelivery.service.impl;

import com.dominos.qa.automation.carsidedelivery.service.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

@Service
public class PowerServiceImpl implements PowerService {

    @Value("${store.id}")
    private String storeId;

    @Autowired
    private WebClient powerClient;

    public Mono<Map> powerOrder(Map request, String path) {
        return powerClient.post()
                .uri(path).accept(APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<ClientResponse> powerCheckIn(Map request) {
        return powerClient.post()
                .uri("pizza")
                .body(BodyInserters.fromObject(request))
                .retrieve()
                .bodyToMono(ClientResponse.class);
    }
}

package com.dominos.qa.automation.carsidedelivery.service.impl;

import com.dominos.qa.automation.carsidedelivery.service.SebsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created by: paripek
 * User: Kavya.Paripelli@dominos.com
 * Date: 07/24/2020
 */

@Service
public class SebsServiceImpl implements SebsService {

    @Value("${store.id}")
    private String storeId;
    @Autowired
    private WebClient sebsClient;

    public Mono<ClientResponse> sendOrderStatus(Map request) {
        return sebsClient.post()
                .uri("/stores/" + storeId + "/messages")
                .header("DPZ-Market", "UNITED_STATES")
                .body(BodyInserters.fromObject(request))
                .exchange();
    }

}

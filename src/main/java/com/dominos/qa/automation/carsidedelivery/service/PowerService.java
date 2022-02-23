package com.dominos.qa.automation.carsidedelivery.service;

import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

public interface PowerService {
    Mono<Map> powerOrder(Map request, String path);

    Mono<ClientResponse> powerCheckIn(Map request);
}

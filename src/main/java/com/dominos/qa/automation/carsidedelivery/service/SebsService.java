package com.dominos.qa.automation.carsidedelivery.service;

import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Created by: paripek
 * User: Kavya.Paripelli@dominos.com
 * Date: 07/24/2020
 */

public interface SebsService {
    Mono<ClientResponse> sendOrderStatus(Map request);
}

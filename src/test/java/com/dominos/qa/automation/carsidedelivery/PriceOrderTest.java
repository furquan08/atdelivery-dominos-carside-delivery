package com.dominos.qa.automation.carsidedelivery;


import com.dominos.qa.automation.carsidedelivery.service.PowerService;
import com.dominos.qa.automation.carsidedelivery.service.UnmarshallerService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

public class PriceOrderTest extends DeliveryPresentationServiceIntegrationTest {

    private static final String POSTAL_CODE_PREFIX = "00000-";
    private static final String path = "price-order";
    private static final File file = new File("src/test/resources/json/priceOrder.json");
    @Value("${store.id}")
    private String storeId;
    @Autowired
    private PowerService powerService;
    @Autowired
    private UnmarshallerService unmarshallerService;
    @Autowired
    private ApplicationState applicationState;
    private Map request;
    private Map response;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @When("^I do price order call for a \"([^\"]*)\" order$")
    public void i_do_price_order_call_for_a_order(String serviceMethod) throws Throwable {
        log.info("Price Order Call to get the payment amount");
        request = unmarshallerService.unmarshall(file);
        updateMapValues(serviceMethod);
        response = powerService.powerOrder(request, path).block();
    }

    @Then("^I get payment amount$")
    public void i_get_payment_amount() throws Throwable {
        log.info("Price Order call response");
        Map order = (Map) response.get("Order");
        Map amounts = (Map) order.get("Amounts");
        Object priceAmount = amounts.get("Payment");
        log.info("price Amount:" + priceAmount);
        assertNotNull(priceAmount);
        applicationState.put("priceAmount", priceAmount);
    }

    private void updateMapValues(String serviceMethod) {
        Map order = (Map) request.get("Order");
        order.put("StoreID", storeId);
        order.put("ServiceMethod", serviceMethod);
        Map address = (Map) order.get("Address");
        address.put("PostalCode", POSTAL_CODE_PREFIX + storeId);
    }
}

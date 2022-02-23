package com.dominos.qa.automation.carsidedelivery;

import com.dominos.qa.automation.carsidedelivery.service.PowerService;
import com.dominos.qa.automation.carsidedelivery.service.UnmarshallerService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Map;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

public class CustomerCheckInTest extends DeliveryPresentationServiceIntegrationTest {

    private static final String path = "pizza";
    @Value("${store.id}")
    private String storeId;
    @Autowired
    private PowerService powerService;
    @Autowired
    private UnmarshallerService unmarshallerService;
    @Autowired
    private ApplicationState applicationState;
    private Map request;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private ClientResponse response;

    @When("^I hit power to checkIn as \"([^\"]*)\" customer$")
    public void i_hit_power_to_checkIn_customer(String orderType) throws Throwable {
        log.info("Customer Checkin");
        request = unmarshallerService.unmarshall(applicationState.getFile("checkIn"));
        updateMapValues(orderType);
        response = powerService.powerCheckIn(request).block();
    }

    private void updateMapValues(String orderType) {
        if (orderType.equals("PHONE")) {
            request.put("alertType", "DUC_IMPROMPTU_ARRIVED");
            Map metadata = (Map) request.get("customerVehicle");
            metadata.put("orderPlacement", request.get("orderPlacement"));
            applicationState.put("ducOrder", metadata);
        } else if (orderType.equals("AUTOCHECKIN")) {
            request.put("alertType", "DUC_GEOFENCE_CUSTOMER_ARRIVED");
            applicationState.put("autoCheckedIn", true);
        }
        request.put("orderId", applicationState.get("pulseOrderGuid"));
        request.put("storeNumber", storeId);
    }
}
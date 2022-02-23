package com.dominos.qa.automation.carsidedelivery;

import com.dominos.ecommerce.dps.domain.event.duc.DucStatusEvent;
import com.dominos.qa.automation.carsidedelivery.service.DeliveryPresentationService;
import com.dominos.qa.automation.carsidedelivery.service.SchedulerService;
import com.dominos.qa.automation.carsidedelivery.service.UnmarshallerService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */


public class DucStateTest extends DeliveryPresentationServiceIntegrationTest {

    @Autowired
    private DeliveryPresentationService deliveryPresentationService;
    @Autowired
    private ApplicationState applicationState;
    @Autowired
    private UnmarshallerService unmarshallerService;
    @Autowired
    private SchedulerService schedulerService;
    @Value("${store.id}")
    private String storeId;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private DucStatusEvent response;
    private ClientResponse clientResponse;
    private DeliveryPresentationServiceTest deliveryPresentationServiceTest;
    private Object ArrayList;


    @When("^I hit service to get DUC state of an order$")
    public void i_hit_service_to_get_fulfillment_state_of_an_order() throws Throwable {
        log.info("Delivery presentation Service Call to get DUCState");
        schedulerService.run(7);
        try {
            response = deliveryPresentationService.getDucStatus().block();
        } catch (WebClientResponseException e) {
            assertEquals(e.getStatusCode().value(), 404);
            log.info(String.valueOf(e.getStatusCode()));
        }
    }

    @Then("^I validate DUC State as (ORDER_PLACED|CUSTOMER_ARRIVED|CUSTOMER_WAITING|TEAM_MEMBER_OUT|COMPLETE|DUC_CLEAR_ORDER|DUC_ELIGIBLE)$")
    public void i_validate_DUC_State_as_(String DUCState) throws Throwable {
        assertNotNull(response);
        assertAll("DUC State",
                () -> assertEquals(DUCState, this.response.getStatus(), "Should get a DUC State"),
                () -> assertEquals(applicationState.get("pulseOrderGuid"), this.response.getOrderId().toString(), "Should get an orderId"));
    }

    @When("^I set DUC order status as \"([^\"]*)\"$")
    public void i_set_DUC_order_status_as(String statusType) throws Throwable {
        log.info("Setting DUC order status " + statusType);
        clientResponse = deliveryPresentationService.setDUCStatus(statusType).block();
        assertEquals(clientResponse.statusCode(), HttpStatus.OK);
    }

    @And("^I validate carside team members list$")
    public void i_validate_carside_team_members_list_() throws Throwable {
        List response = deliveryPresentationService.getCarsideTeamMembers().block();
        assertNotNull(response, "Should get a list of available team members");
        assertTrue(response.size() > 0, "Team mebers list should not be empty");
    }

    @When("^I dispatch carside order with available team members$")
    public void i_dispatch_carside_order_with_available_team_members_() throws Throwable {
        int status = 0;
        int count = 0;
        Map request = unmarshallerService.unmarshall(applicationState.getFile("carsideDispatch"));
        updateMapValues(request);
        while (count < 2 && status != 200) {
            schedulerService.run(5);
            clientResponse = deliveryPresentationService.carsideDispatches(request).block();
            status = clientResponse.statusCode().value();
            count++;
        }
        assertEquals(clientResponse.statusCode(), HttpStatus.OK);
    }

    private void updateMapValues(Map request) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        request.put("businessDate", dateFormat.format(currentDate));
        request.put("storeId", storeId);
        request.put("orderNumber", Integer.valueOf(applicationState.get("StoreOrderID").toString().split("#")[1]));
    }

}
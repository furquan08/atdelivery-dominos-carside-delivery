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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */
public class PlaceOrderTest extends DeliveryPresentationServiceIntegrationTest {

    private static final String POSTAL_CODE_PREFIX = "00000-";
    private static final String PLACE_ORDER_STATUS_CODE = "Warning";
    private static final String path = "place-order";
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

    @When("^I do place order call for \"([^\"]*)\" order$")
    public void i_do_place_order_call_for_order(String paymentType) throws Throwable {
        log.info("Price Order Call to get the payment amount");
        String[] input = paymentType.split("-");
        if (input[0].equals("CASH")) {
            request = unmarshallerService.unmarshall(applicationState.getFile("cashPlaceOrder"));
        } else
            request = unmarshallerService.unmarshall(applicationState.getFile("ecommPlaceOrder"));
        updateMapValues(input[0], input[1]);
        response = powerService.powerOrder(request, path).block();
    }

    @Then("^I validate that order was created for \"([^\"]*)\" order$")
    public void i_validate_that_order_was_created(String orderType) throws Throwable {
        log.info("Place order response");
        List statusItemsList = (List) response.get("StatusItems");
        Map codeMap = (Map) statusItemsList.get(0);
        assertEquals(PLACE_ORDER_STATUS_CODE, codeMap.get("Code"), "Should get code value as Warning");
        if (orderType.equals("CASH")) {
            Map order = (Map) response.get("Order");
            String pulseOrderGuid = (String) order.get("PulseOrderGuid");
            assertNotNull(pulseOrderGuid);
            applicationState.put("pulseOrderGuid", pulseOrderGuid);
        } else {
            Map order = (Map) response.get("Order");
            String pulseOrderGuid = (String) order.get("PulseOrderGuid");
            assertNotNull(pulseOrderGuid);
            applicationState.put("pulseOrderGuid", pulseOrderGuid);
            applicationState.put("FirstName", order.get("FirstName"));
            applicationState.put("LastName", order.get("LastName"));
            applicationState.put("customerPhoneNumber", order.get("Phone"));
            applicationState.put("ServiceMethod", order.get("ServiceMethod"));
            applicationState.put("StoreOrderID", order.get("StoreOrderID"));
            List products = (List) order.get("Products");
            Map product = (Map) products.get(0);
            applicationState.put("productQty", product.get("Qty"));
            applicationState.put("productName", product.get("Code"));
            Map metadata = (Map) order.get("metaData");
            assertNotNull(metadata.get("autoCheckInEnabled"));
            applicationState.put("autoCheckInEnabled", metadata.get("autoCheckInEnabled"));
            applicationState.put("autoCheckedIn", false);
            applicationState.put("ducOrder", metadata.get("ducOrder"));
            if (orderType.equals("Carside")) {
                assertEquals(order.get("ServiceMethod"), "Carryout");
                assertEquals(metadata.get("OriginalServiceMethod"), orderType);
            }
        }

    }

    private void updateMapValues(String orderType, String serviceMethod) {
        Map order = (Map) request.get("Order");
        order.put("StoreID", storeId);
        order.put("ServiceMethod", serviceMethod);
        Map address = (Map) order.get("Address");
        address.put("PostalCode", POSTAL_CODE_PREFIX + storeId);
        List payments = (List) order.get("Payments");
        Map payment = (Map) payments.get(0);
        payment.put("Amount", applicationState.get("priceAmount"));
        Map metadata = (Map) order.get("metaData");
        if (orderType.equals("APP")) {
            order.put("SourceOrganizationURI", "android.dominos.com");
            metadata.put("autoCheckInEnabled", true);
        }
    }

}
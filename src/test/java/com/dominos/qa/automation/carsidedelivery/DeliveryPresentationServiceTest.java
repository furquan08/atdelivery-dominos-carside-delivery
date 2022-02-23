package com.dominos.qa.automation.carsidedelivery;

import com.dominos.qa.automation.carsidedelivery.service.DeliveryPresentationService;
import com.dominos.qa.automation.carsidedelivery.service.FulfillmentService;
import com.dominos.qa.automation.carsidedelivery.service.SchedulerService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class DeliveryPresentationServiceTest extends DeliveryPresentationServiceIntegrationTest {
    @Value("${store.id}")
    private String storeId;
    @Autowired
    private DeliveryPresentationService deliveryPresentationService;
    @Autowired
    private FulfillmentService fulfillmentService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private ApplicationState applicationState;
    @Autowired
    private ObjectMapper objectMapper;

    @When("^I verify DUC order status in \"([^\"]*)\" for \"([^\"]*)\" event as \"([^\"]*)\"$")
    public void i_verify_DUC_order_status_in_for_event_as_endpoint(String service, String eventType, String statusType) throws Throwable {
        schedulerService.run(10);
        Flux<ServerSentEvent<String>> storeResponse = null;
        if (service.equals("fulfillment-service")) {
            storeResponse = fulfillmentService.subscribe(eventType);
        } else {
            storeResponse = deliveryPresentationService.subscribe();
        }
        assertNotNull(storeResponse);
        storeResponse.subscribe(sse -> {
            if (sse.data() != null && sse.data().contains(applicationState.get("pulseOrderGuid").toString())) {
                ApplicationState.eventsMap.put(sse.event(), sse.data());
            }
        });
        schedulerService.run(3);
        assertNotNull(storeResponse);
        String data = ApplicationState.eventsMap.get(eventType);
        assertNotNull(data);
        Map response = objectMapper.readValue(data, Map.class);
        validateEvents(service, eventType, response, statusType);
    }

    private void validateEvents(String service, String eventType, Map response, String statusType) {
        if (eventType.equals("FLATTENED_ORDER")) {
            validateDUCFlattenedOrderEvent(eventType, service, response, statusType);
        } else if (eventType.equals("DRIVE_UP_CARRYOUT_ORDER")) {
            validateDriveUpCarryoutOrderEvent(eventType, service, response, statusType);
        } else if (eventType.equals("DRIVE_UP_CARRYOUT")) {
            validateDriveUpCarryoutEvent(eventType, service, response, statusType);
        } else if (eventType.equals("DRIVE_UP_CARRYOUT_STATUS")) {
            validateDriveUpCarryoutStatusEvent(response, statusType);
        }
    }

    public void validateDUCFlattenedOrderEvent(String eventType, String service, Map response, String statusType) {
        assertEquals(response.get("status"), statusType);
        assertEquals(response.get("firstName"), applicationState.get("FirstName"));
        assertEquals(response.get("lastName"), applicationState.get("LastName"));
        assertEquals(response.get("customerPhoneNumber"), applicationState.get("customerPhoneNumber"));
        assertEquals(response.get("serviceMethodCode").toString().toUpperCase(), applicationState.get("ServiceMethod").toString().toUpperCase());
        validateProductDetails(service, response);
        validateCustomerVehicle(eventType, service, response);
    }

    private void validateDriveUpCarryoutOrderEvent(String eventType, String service, Map response, String statusType) {
        assertAll("DRIVE_UP_CARRYOUT_ORDER Event Validation",
                () -> assertEquals(response.get("status"), statusType),
                () -> assertEquals(response.get("firstName"), applicationState.get("FirstName")),
                () -> assertEquals(response.get("lastName"), applicationState.get("LastName")),
                () -> assertEquals(response.get("customerPhoneNumber"), applicationState.get("customerPhoneNumber")),
                () -> assertEquals(String.valueOf(response.get("orderNumber")), applicationState.get("StoreOrderID").toString().split("#")[1]));
        if (service.equals("fulfillment-service")) {
            validateCustomerVehicle(eventType, service, response);
        }
        validateProductDetails(service, response);
    }

    private void validateDriveUpCarryoutEvent(String eventType, String service, Map response, String statusType) {
        assertAll("DRIVE_UP_CARRYOUT Event Validation",
                () -> assertEquals(response.get("status"), statusType),
                () -> assertEquals(response.get("autoCheckInEnabled"), applicationState.get("autoCheckInEnabled")),
                () -> assertEquals(response.get("autoCheckedIn"), applicationState.get("autoCheckedIn")));
        validateCustomerVehicle(eventType, service, response);
    }

    public void validateDriveUpCarryoutStatusEvent(Map response, String statusType) {
        assertEquals(response.get("status"), statusType);
    }

    private void validateCustomerVehicle(String eventType, String service, Map response) {
        Map<String, Object> requestDucOrder = (Map<String, Object>) applicationState.get("ducOrder");
        if (service.equals("fulfillment-service") && eventType.contains("ORDER")) {
            Map<String, Object> responseMetaData = (Map<String, Object>) response.get("metadata");
            Map<String, Object> customerVehicleInfo = (Map<String, Object>) responseMetaData.get("customerVehicle");
            assertAll("Customer Vehicle Info Validation",
                    () -> assertEquals(customerVehicleInfo.get("make"), requestDucOrder.get("make")),
                    () -> assertEquals(customerVehicleInfo.get("model"), requestDucOrder.get("model")),
                    () -> assertEquals(customerVehicleInfo.get("color"), requestDucOrder.get("color")),
                    () -> assertEquals(responseMetaData.get("orderPlacement"), requestDucOrder.get("orderPlacement")));
        } else {
            Map<String, Object> customerVehicleInfo = (Map<String, Object>) response.get("customerVehicle");
            assertAll("Customer Vehicle Info Validation",
                    () -> assertEquals(customerVehicleInfo.get("make"), requestDucOrder.get("make")),
                    () -> assertEquals(customerVehicleInfo.get("model"), requestDucOrder.get("model")),
                    () -> assertEquals(customerVehicleInfo.get("color"), requestDucOrder.get("color")),
                    () -> assertEquals(response.get("orderPlacement"), requestDucOrder.get("orderPlacement")));
        }


    }

    private void validateProductDetails(String service, Map response) {
        if (service.equals("fulfillment-service")) {
            List products = (List) response.get("products");
            Map product = (Map) products.get(0);
            assertAll("Product Details Validation",
                    () -> assertEquals(product.get("quantity"), applicationState.get("productQty")),
                    () -> assertEquals(product.get("code"), applicationState.get("productName")));
        } else {
            Map responseOrderLines = (Map) response.get("orderLines");
            List products = (List) responseOrderLines.get("lines");
            Map product = (Map) products.get(0);
            assertAll("Product Details Validation",
                    () -> assertEquals(product.get("quantity"), applicationState.get("productQty")),
                    () -> assertEquals(product.get("code"), applicationState.get("productName")));
        }

    }
}

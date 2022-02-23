package com.dominos.qa.automation.carsidedelivery;

import com.dominos.qa.automation.carsidedelivery.service.SebsService;
import com.dominos.qa.automation.carsidedelivery.service.UnmarshallerService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import com.dominos.qa.automation.carsidedelivery.util.UUIDGenerator;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by: paripek
 * User: Kavya.Paripelli@dominos.com
 * Date: 07/24/2020
 */

@Slf4j
public class SebsTest {
    private Map request;
    private ClientResponse response;
    @Value("${store.id}")
    private String storeId;
    @Autowired
    private SebsService sebsService;
    @Autowired
    private UnmarshallerService unmarshallerService;
    @Autowired
    private ApplicationState applicationState;


    @When("^I hit SEBS with \"([^\"]*)\" status for \"([^\"]*)\" order$")
    public void i_hit_sebs_with_status(String orderStatus, String orderType) throws Throwable {
        log.info("Sending " + orderStatus + " status");
        request = unmarshallerService.unmarshall(applicationState.getFile(orderStatus));
        updateMapValues(orderStatus, orderType);
        TimeUnit.SECONDS.sleep(10);
        response = sebsService.sendOrderStatus(request).block();

    }

    private void updateMapValues(String orderStatus, String orderType) {
        request.put("eventType", orderStatus);
        request.put("publishedAt", OffsetDateTime.now().toString());
        List messages = (List) request.get("messages");
        Map message = (Map) messages.get(0);
        message.put("correlationId", UUIDGenerator.generate());
        message.put("eventAt", OffsetDateTime.now().toString());
        Map data = (Map) message.get("data");
        data.put("storeNumber", storeId);
        if (orderStatus.equals("OrderAccepted") && orderType.equals("PHONE")) {
            List orderLines = (List) data.get("orderLines");
            Map products = (Map) orderLines.get(0);
            Map product = (Map) products.get("preparedProduct");
            String pulseOrderGuid = UUIDGenerator.generate().toString();
            assertNotNull(pulseOrderGuid);
            applicationState.put("pulseOrderGuid", pulseOrderGuid);
            applicationState.put("FirstName", "KAVYA");
            applicationState.put("LastName", "PARIPELLI");
            applicationState.put("customerPhoneNumber", data.get("phoneNumber"));
            applicationState.put("ServiceMethod", data.get("serviceMethodGroup"));
            applicationState.put("StoreOrderID", "#" + data.get("storeOrderId"));
            applicationState.put("productQty", products.get("quantity"));
            applicationState.put("productName", product.get("code"));
            applicationState.put("autoCheckInEnabled", false);
            applicationState.put("autoCheckedIn", false);
            data.put("pulseOrderGuid", pulseOrderGuid);
            data.put("orderMethod", orderType);
            data.put("eCommOrderId", "");
        } else {
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            data.put("businessDate", dateFormat.format(currentDate));
            data.put("pulseOrderGuid", applicationState.get("pulseOrderGuid"));
            data.put("storeOrderId", applicationState.get("StoreOrderID").toString().split("#")[1]);
            data.put("orderNumber", applicationState.get("StoreOrderID").toString().split("#")[1]);
            data.put("storeAsOfTime", OffsetDateTime.now().toString());
            if (orderType.equals("Carside")) {
                data.put("serviceMethod", orderType);
                data.put("serviceMethodCode", "X");
                applicationState.put("ServiceMethod", orderType.toUpperCase());
            }
        }
    }
}

package com.dominos.qa.automation.carsidedelivery;

import com.dominos.qa.automation.carsidedelivery.service.SchedulerService;
import com.dominos.qa.automation.carsidedelivery.service.UnmarshallerService;
import com.dominos.qa.automation.carsidedelivery.util.ApplicationState;
import com.dominos.qa.automation.carsidedelivery.util.DateGenerator;
import com.dominos.qa.automation.carsidedelivery.util.UUIDGenerator;
import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by: paripek
 * User: Kavya.Paripelli@dominos.com
 * Date: 07/24/2020
 */

@Slf4j
public class OrderPlacedEventTest {
    private static final String date = "yyyy-MM-dd";

    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    private static final File file = new File("src/test/resources/json/OrderPlaced.json");

    @Value("${store.id}")
    private String storeId;
    @Autowired
    private UnmarshallerService unmarshallerService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private ApplicationState applicationState;
    @Autowired
    private JmsTemplate jmsTemplate;
    private Map orderPlaced;
    private String pulseOrderGuid;


    @Given("^I published DUC order placed message for a \"([^\"]*)\" order$")
    public void i_published_DUC_order_placed_message(String serviceMethod) throws Throwable {
        log.info("Publishing Order Placed Event");
        pulseOrderGuid = UUIDGenerator.generate().toString();
        applicationState.put("pulseOrderGuid", pulseOrderGuid);
        orderPlaced = unmarshallerService.unmarshall(file);
        applicationState.put("FirstName", orderPlaced.get("FirstName"));
        applicationState.put("LastName", orderPlaced.get("LastName"));
        applicationState.put("customerPhoneNumber", orderPlaced.get("Phone"));
        applicationState.put("ServiceMethod", orderPlaced.get("ServiceMethod"));
        applicationState.put("StoreOrderID", orderPlaced.get("StoreOrderID"));
        applicationState.put("autoCheckInEnabled", false);
        applicationState.put("autoCheckedIn", false);
        List products = (List) orderPlaced.get("Products");
        Map product = (Map) products.get(0);
        applicationState.put("productQty", product.get("Qty"));
        applicationState.put("productName", product.get("Code"));
        Map metadata = (Map) orderPlaced.get("metaData");
        applicationState.put("ducOrder", metadata.get("ducOrder"));
        updateMapValues(serviceMethod);
        schedulerService.run(5);
        this.jmsTemplate.convertAndSend(orderPlaced);
        log.info("Order Placed event is published successfully");
    }

    private void updateMapValues(String serviceMethod) {
        orderPlaced.put("StoreID", storeId);
        orderPlaced.put("PulseOrderGuid", pulseOrderGuid);
        orderPlaced.put("BusinessDate", DateGenerator.generateDate(date));
        orderPlaced.put("StorePlaceOrderTime", DateGenerator.generateDate(pattern));
        orderPlaced.put("PlaceOrderTime", DateGenerator.generateDate(pattern));
        if (serviceMethod.equals("Carside")) {
            Map metadata = (Map) orderPlaced.get("metaData");
            metadata.put("OriginalServiceMethod", serviceMethod);
            orderPlaced.put("metaData", metadata);
        }
    }
}

package com.dominos.qa.automation.carsidedelivery;

import com.dominos.qa.automation.carsidedelivery.service.SchedulerService;
import cucumber.api.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TTLTest {

    @Autowired
    private SchedulerService schedulerService;

    @And("^I waited for (\\d+) minutes with no further activity$")
    public void i_waited_for_minutes_with_no_further_activity(int time) {
        try {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            log.info("Waiting for " + time + " minutes with no activity " + formatter.format(date));
            schedulerService.run(61 * time);
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage());
        }
    }
}

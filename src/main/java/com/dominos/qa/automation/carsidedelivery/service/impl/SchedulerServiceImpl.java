package com.dominos.qa.automation.carsidedelivery.service.impl;

import com.dominos.qa.automation.carsidedelivery.Exception.BusinessException;
import com.dominos.qa.automation.carsidedelivery.service.SchedulerService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SchedulerServiceImpl implements SchedulerService {


    @Override
    public void run(Integer timeToSleep) {

        try {
            TimeUnit.SECONDS.sleep(timeToSleep);
        } catch (InterruptedException ioe) {
            throw new BusinessException("Unable to run scheduler", ioe);
        }
    }
}

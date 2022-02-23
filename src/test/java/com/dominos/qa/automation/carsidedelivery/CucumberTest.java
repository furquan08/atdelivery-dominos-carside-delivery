package com.dominos.qa.automation.carsidedelivery;


import com.vimalselvam.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",
        monochrome = true,
        plugin = {"json:target/cucumber.json", "com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:target/report.html"},
        glue = {"com.dominos.qa.automation.deliverypresentationservice"})

public class CucumberTest {

    @AfterClass
    public static void teardown() {
        Reporter.loadXMLConfig(new File("src/test/resources/config/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("Test Environment", System.getProperty("env"));
        Reporter.setTestRunnerOutput("Fulfillment Service Automation Results");
    }
}

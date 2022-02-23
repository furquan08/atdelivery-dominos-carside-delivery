package com.dominos.qa.automation.carsidedelivery.service;

import java.io.File;
import java.io.IOException;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

public interface UnmarshallerService {

    <T> T unmarshall(File jsonFile) throws IOException;
}

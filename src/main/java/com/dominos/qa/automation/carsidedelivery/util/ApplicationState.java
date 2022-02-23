package com.dominos.qa.automation.carsidedelivery.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

@Component
public class ApplicationState {

    public static Map<String, String> eventsMap = new HashMap<>();
    private final Map<String, Object> map = new ConcurrentHashMap();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public File getFile(String name) {
        return new File("src/test/resources/json/" + name + ".json");
    }
}

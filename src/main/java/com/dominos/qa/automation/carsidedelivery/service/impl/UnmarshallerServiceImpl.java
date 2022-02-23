package com.dominos.qa.automation.carsidedelivery.service.impl;

import com.dominos.qa.automation.carsidedelivery.service.UnmarshallerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */


@Service
public class UnmarshallerServiceImpl implements UnmarshallerService {

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void setup() {
        mapper.registerModule(new JavaTimeModule());
    }

    public <T> T unmarshall(File jsonFile) throws IOException {
        InputStream inputStream = new FileInputStream(jsonFile);
        JsonNode jsonNode = mapper.readTree(inputStream);
        return mapper.treeToValue(jsonNode, ((Class<T>) getClass().getGenericSuperclass()));
    }
}

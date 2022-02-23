package com.dominos.qa.automation.carsidedelivery.util;

import com.dominos.qa.automation.carsidedelivery.Exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

@Slf4j
@Component
public class JmsMessageBuilder implements MessageConverter {

    private ObjectMapper mapper;

    public JmsMessageBuilder() {
        mapper = new ObjectMapper();
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException {
        Map data = (Map) object;
        String payload = null;
        try {
            payload = mapper.writeValueAsString(data);
            log.info("outbound json='{}'", payload);
        } catch (Exception e) {
            throw new BusinessException("error in processing Json");
        }
        TextMessage message = session.createTextMessage();
        message.setText(payload);
        return message;
    }

    @Override
    public Object fromMessage(Message message) {
        throw new MessageConversionException("Not implemented");
    }
}

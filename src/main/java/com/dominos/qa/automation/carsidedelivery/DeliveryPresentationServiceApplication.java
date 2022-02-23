package com.dominos.qa.automation.carsidedelivery;

import com.dominos.qa.automation.carsidedelivery.util.JmsMessageBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.jms.ConnectionFactory;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by: yarlagp
 * User: pravallika.yarlagadda@dominos.com
 * Date: 10/22/19
 */

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
public class DeliveryPresentationServiceApplication {

    @Value("${power.order.url}")
    private String powerOrderUrl;
    @Value("${delivery.presentation.service.url}")
    private String deliveryPresentationUrl;
    @Value("${fs.url}")
    private String fsUrl;
    @Value("${sebs.url}")
    private String sebsUrl;
    @Value("${activemq.power.broker.url}")
    private String brokerUrl;
    @Value("${place.order.queue}")
    private String powerQueue;
    @Value("${dps.api.key}")
    private String dpsApiKey;
    @Value("${fs.api.key}")
    private String fsApiKey;

    public static void main(String[] args) {
        SpringApplication.run(DeliveryPresentationServiceApplication.class, args);
    }

    @Bean
    public WebClient powerClient() {
        return WebClient
                .builder()
                .baseUrl(powerOrderUrl)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader("DPZ-Language", "en")
                .defaultHeader("DPZ-Market", "UNITED_STATES")
                .build();
    }

    @Bean
    public WebClient dpsClient() {
        return WebClient
                .builder()
                .baseUrl(deliveryPresentationUrl)
                .defaultHeader("DPZ-Api-Key", dpsApiKey)
                .build();
    }

    @Bean
    public WebClient fsClient() {
        return WebClient
                .builder()
                .baseUrl(fsUrl)
                .defaultHeader("Accept", "text/event-stream")
                .defaultHeader("DPZ-Api-Key", fsApiKey)
                .build();
    }

    @Bean
    public WebClient sebsClient() {
        return WebClient
                .builder()
                .baseUrl(sebsUrl)
                .defaultHeader("source", "Store-message-bridge-V0.0.1")
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        return connectionFactory;
    }

    @Bean
    public ActiveMQQueue queueBean() {
        return new ActiveMQQueue(powerQueue);
    }

    @Bean
    public JmsMessageBuilder jmsMessageConverter() {
        return new JmsMessageBuilder();
    }

    @Bean
    public JmsTemplate jmsTemplateBean() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestination(queueBean());
        template.setMessageConverter(jmsMessageConverter());
        return template;
    }
}



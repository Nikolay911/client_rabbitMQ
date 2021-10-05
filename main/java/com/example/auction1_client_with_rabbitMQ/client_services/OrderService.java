package com.example.auction1_client_with_rabbitMQ.client_services;

import com.example.auction1_client_with_rabbitMQ.config.URLconfigProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class OrderService extends RestTemplateService{

    private final URLconfigProperties urLconfigProperties;

    public OrderService(URLconfigProperties urLconfigProperties) {
        this.urLconfigProperties = urLconfigProperties;
    }


    public String bid(int idCustomer, int idAuction, JsonNode body){

        String order = restTemplate.postForObject(
                urLconfigProperties.getBid() + idAuction + "/customer/" + idCustomer, body, String.class);

        return order;
    }
}


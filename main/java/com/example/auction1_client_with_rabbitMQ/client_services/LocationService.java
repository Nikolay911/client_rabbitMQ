package com.example.auction1_client_with_rabbitMQ.client_services;

import com.example.auction1_client_with_rabbitMQ.config.URLconfigProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LocationService extends RestTemplateService{

    private final URLconfigProperties urLconfigProperties;

    public LocationService(URLconfigProperties urLconfigProperties) {
        this.urLconfigProperties = urLconfigProperties;
    }

    @PreAuthorize("@customExpressions.isHisCustomer(#idCustomer, #request)")
    public Boolean createLocation(int idCustomer, JsonNode body, HttpServletRequest request){

        Boolean success = restTemplate.postForObject(urLconfigProperties.getCreateCustomerLocation() + idCustomer, body, Boolean.class);

        return success;
    }

}

package com.example.auction1_client_with_rabbitMQ.client_customerController;

import com.example.auction1_client_with_rabbitMQ.client_services.LocationService;
import com.example.auction1_client_with_rabbitMQ.mq_services.producer.LocationProduser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/client")
public class LocationController {

    private final LocationService locationService;
    private final LocationProduser locationProduser;

    public LocationController(LocationService locationService, LocationProduser locationProduser) {
        this.locationService = locationService;
        this.locationProduser = locationProduser;
    }


    @PostMapping("/location/customer/{CustomerId}")
    public String createLocation(@PathVariable int CustomerId,
                                  @RequestBody JsonNode body,
                                  HttpServletRequest request) {

        locationProduser.CreateLocationToCustomer(CustomerId,body, request);

        return "запрос отправлен";
    }
}

package com.example.auction1_client_with_rabbitMQ.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "url", ignoreUnknownFields = false)
public class URLconfigProperties {

    private String getCustomer;
    private String getCustomerLocations;
    private String createCustomer;
    private String getAllCustomerWithLocations;
    private String createCustomerLocation;
    private String createProduct;
    private String getProduct;
    private String createAuction;
    private String bid;
    private String getAuction;
    private String getAllAuctionBid;
    private String  getAllAuction;

}

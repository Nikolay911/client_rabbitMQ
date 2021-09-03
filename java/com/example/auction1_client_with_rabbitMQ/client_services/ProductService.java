package com.example.auction1_client_with_rabbitMQ.client_services;

import com.example.auction1_client_with_rabbitMQ.client_models.Product;
import com.example.auction1_client_with_rabbitMQ.config.URLconfigProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ProductService extends RestTemplateService{

    private final URLconfigProperties urLconfigProperties;

    public ProductService(URLconfigProperties urLconfigProperties) {
        this.urLconfigProperties = urLconfigProperties;
    }

    @PreAuthorize("@customExpressions.isHisCustomer(#idCustomer, #request)")
    public Product createProduct(int idCustomer, JsonNode body, HttpServletRequest request){

        Product product = restTemplate.postForObject(urLconfigProperties.getCreateProduct() + idCustomer, body, Product.class);

        return product;
    }

    public Product getProduct(int idProduct){

        Product product = restTemplate.getForObject(urLconfigProperties.getGetProduct() + idProduct, Product.class);

        return product;
    }
}

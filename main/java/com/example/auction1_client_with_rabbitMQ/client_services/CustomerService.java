package com.example.auction1_client_with_rabbitMQ.client_services;

import com.example.auction1_client_with_rabbitMQ.client_models.Customer;
import com.example.auction1_client_with_rabbitMQ.client_models.Location;
import com.example.auction1_client_with_rabbitMQ.client_models.User;
import com.example.auction1_client_with_rabbitMQ.client_services.login.GeneralService;
import com.example.auction1_client_with_rabbitMQ.config.URLconfigProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomerService extends RestTemplateService {

    private final URLconfigProperties urLconfigProperties;
    private final GeneralService generalService;

    public CustomerService(URLconfigProperties urLconfigProperties, GeneralService generalService) {
        this.urLconfigProperties = urLconfigProperties;
        this.generalService = generalService;
    }

    @PreAuthorize("@customExpressions.isHisCustomer(#idCustomer, #request)")
    public Customer getCustomer(int idCustomer, HttpServletRequest request) throws JsonProcessingException {

        Customer customer = restTemplate.getForObject(urLconfigProperties.getGetCustomer() + idCustomer, Customer.class);
        System.out.println(customer.toString());

        List<Location> locationList = customerLocations(idCustomer);

        customer.setLocations(locationList);

        return customer;
    }

    public JsonNode createCustomer(JsonNode body, HttpServletRequest request) throws Exception {

        String username = request.getRemoteUser();
        User user = generalService.findByUsername(username);

        if(user.getCustomerId() != 0){
            throw new Exception("Customer уже был создан");
        }

        JsonNode cust = restTemplate.postForObject(urLconfigProperties.getCreateCustomer(), body, JsonNode.class);

        int customerId = cust.findValue("id").asInt();

        generalService.addCustomer(username, customerId);

        return cust;
    }

    public List<Customer> getAllCustomer(String surname, String city) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = restTemplate.getForObject(urLconfigProperties.getGetAllCustomerWithLocations(), String.class);
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        List<Customer> allCustomer = objectMapper.convertValue(jsonNode,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Customer.class));

        List<Customer> customerWithLocations = allCustomer.stream().peek(customer -> {
            try {
                customer.setLocations(customerLocations(customer.getId()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }).collect(Collectors.toList());

        List<Customer> filterSurname = customerWithLocations.stream()
                .filter(customer -> {if(!(surname == null)) return customer.getSurname().equals(surname);
                return true;})
                .filter(customer -> {if(!(city == null))
                    return customer.getLocations().stream().filter(y -> y.getCity().equals(city)).count()>0;
                    return true;})
                    .collect(Collectors.toList());

        return filterSurname;
    }

    public List<Location> customerLocations(int idCustomer) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = restTemplate.getForObject(urLconfigProperties.getGetCustomerLocations()+idCustomer , String.class);
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        List<Location> customerLocations = objectMapper.convertValue(jsonNode,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Location.class));

        return customerLocations;
    }
}

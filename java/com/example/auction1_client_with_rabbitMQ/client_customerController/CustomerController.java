package com.example.auction1_client_with_rabbitMQ.client_customerController;

import com.example.auction1_client_with_rabbitMQ.client_services.CustomerService;
import com.example.auction1_client_with_rabbitMQ.client_models.Customer;
import com.example.auction1_client_with_rabbitMQ.mq_services.consumer.GeneralConsumer;
import com.example.auction1_client_with_rabbitMQ.mq_services.producer.CustomerProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/client")
@RabbitListener(queues = "${rabbitmq.queue2}")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerProducer customerProducer;

    private final GeneralConsumer generalConsumer;


    public CustomerController(CustomerService customerService, CustomerProducer customerProducer, GeneralConsumer generalConsumer) {
        this.customerService = customerService;
        this.customerProducer = customerProducer;
        this.generalConsumer = generalConsumer;
    }


    @GetMapping("/customer/{id}")
    public String getCustomer(@PathVariable int id,
                                HttpServletRequest request) throws JsonProcessingException {

        customerProducer.GetCustomerById("GetCustomerById", id, request);

        return "запрос отправлен";
        //return customerService.getCustomer(id, request);
    }

    @PostMapping("/customer")
    public String createCustomer(@RequestBody JSONObject body,
                                   HttpServletRequest request) throws Exception {
        customerProducer.createCustomer(body);
        return "запрос отправлен";
    }

    @GetMapping("/all/customer")
    public String getAllCustomers(@RequestParam(required = false) String surname,
                                          @RequestParam(required = false) String city) throws JsonProcessingException, InterruptedException {
        customerProducer.GetAllCustomer();
        return "Запрос отправлен";
        //return customerService.getAllCustomer(surname, city);
    }
}

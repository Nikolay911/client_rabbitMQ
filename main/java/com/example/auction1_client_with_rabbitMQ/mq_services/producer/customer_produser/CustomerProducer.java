package com.example.auction1_client_with_rabbitMQ.mq_services.producer.customer_produser;


import com.example.auction1_client_with_rabbitMQ.client_models.User;
import com.example.auction1_client_with_rabbitMQ.mq_services.producer.GeneralProduser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.core.ApplicationContext;
import org.json.simple.JSONObject;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import com.functions.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.sql.Date;
import java.util.Properties;

@Component
public class CustomerProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GeneralProduser produser;

    public void GetAllCustomer(){
        com.functions.GetAllCustomer getAllCustomer = new com.functions.GetAllCustomer();
        try {
            rabbitTemplate.convertAndSend(produser.getQueue(), getAllCustomer);
        }
        catch (AmqpException the_exception){
            System.out.println("В соединении отказано. Проблема возникает при попытке подключиться к RabbitMQ");
        }
        catch (Exception ex){
            System.out.println("Неопознанное исключение");
        }
    }

    @PreAuthorize("@customExpressions.isHisCustomer(#id, #request)")
    public void GetCustomerById(String requestName, int id, HttpServletRequest request){

        com.functions.GetCustomerById getCustomerById = new com.functions.GetCustomerById(id);

        try {
            rabbitTemplate.convertAndSend(produser.getQueue(), getCustomerById);
        }
        catch (AmqpException the_exception){
            System.out.println("В соединении отказано. Проблема возникает при попытке подключиться к RabbitMQ");
        }
        catch (Exception ex){
            System.out.println("Неопознанное исключение");
        }

    }

    public void createCustomer(JSONObject body) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(body, JsonNode.class);

        String date1 = jsonNode.get("dateOfBirth").toString();
        date1 = date1.substring(1, date1.length()-1);
        Date date = Date.valueOf(date1);
        com.functions.CreateCustomer createCustomer = new com.functions.CreateCustomer(jsonNode.get("name").toString(), jsonNode.get("surname").toString(),
                                                                                    jsonNode.get("patronymic").toString(), date);
        try {
            rabbitTemplate.convertAndSend(produser.getQueue(), createCustomer);
        }
        catch (AmqpException the_exception){
            System.out.println("В соединении отказано. Проблема возникает при попытке подключиться к RabbitMQ");
        }
        catch (Exception ex){
            System.out.println("Неопознанное исключение");
        }

    }
}

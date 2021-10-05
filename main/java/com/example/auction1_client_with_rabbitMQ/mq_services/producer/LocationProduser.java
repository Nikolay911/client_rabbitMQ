package com.example.auction1_client_with_rabbitMQ.mq_services.producer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LocationProduser {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GeneralProduser produser;


    @PreAuthorize("@customExpressions.isHisCustomer(#idCustomer, #request)")
    public void  CreateLocationToCustomer(int idCustomer, JsonNode body, HttpServletRequest request){

        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "CreateLocationToCustomer");
        props.setContentType("text/plain");

        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = mapper.convertValue(body, JSONObject.class);
        jsonObject.put("id", idCustomer);

        JsonNode jsonNode = mapper.convertValue(jsonObject, JsonNode.class);

        Message msg = new Message(jsonNode.toString().getBytes(), props);

        try {
            rabbitTemplate.convertAndSend(produser.getQueue(), msg);
        }
        catch (AmqpException the_exception){
            System.out.println("В соединении отказано. Проблема возникает при попытке подключиться к RabbitMQ");
        }
        catch (Exception ex){
            System.out.println("Неопознанное исключение");
        }
    }
}

package com.example.auction1_client_with_rabbitMQ.mq_services.producer;


import com.example.auction1_client_with_rabbitMQ.client_models.User;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomerProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GeneralProduser produser;

    public void GetAllCustomer(){
        JSONObject jsonObject = new JSONObject();
        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "GetAllCustomer");
        props.setContentType("text/plain");

        Message msg = new Message(jsonObject.toString().getBytes(), props);
        rabbitTemplate.convertAndSend(produser.getQueue(), msg);
    }

    @PreAuthorize("@customExpressions.isHisCustomer(#id, #request)")
    public void GetCustomerById(String requestName, int id, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestName", requestName);
        jsonObject.put("CustomerId", id);

        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", requestName);
        props.setContentType("text/plain");

        Message msg = new Message(jsonObject.toString().getBytes(), props);
        rabbitTemplate.convertAndSend(produser.getQueue(), msg);
    }

    public void createCustomer(JSONObject body) throws Exception {

        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "createCustomer");
        props.setContentType("text/plain");


        Message msg = new Message(body.toString().getBytes(), props);
        rabbitTemplate.convertAndSend(produser.getQueue(), msg);

    }
}

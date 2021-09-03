package com.example.auction1_client_with_rabbitMQ.mq_services.producer;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProduser {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GeneralProduser produser;

    public void Bid(int customerId, int auctionId, JsonNode body){
        JSONObject jsonObject = new JSONObject();

        double price = Double.valueOf(body.get("customerPrice").toString());

        jsonObject.put("customerPrice", price);
        jsonObject.put("customerId", customerId);
        jsonObject.put("auctionId", auctionId);

        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "Bid");
        props.setContentType("text/plain");

        Message msg = new Message(jsonObject.toString().getBytes(), props);
        rabbitTemplate.convertAndSend(produser.getQueue(), msg);
    }
}

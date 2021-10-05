package com.example.auction1_client_with_rabbitMQ.mq_services.producer;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONObject;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class AuctionProduser {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GeneralProduser produser;

    public void CreateAuction(int customerId, int productId, JsonNode body){
        JSONObject jsonObject = new JSONObject();

        String time = body.get("auction_completion_date").toString();
        time = time.substring(1);
        int size = time.length();
        time = time.substring(0,size-1);

        Timestamp timestamp = Timestamp.valueOf(time);

        jsonObject.put("customerId", customerId);
        jsonObject.put("productId", productId);
        jsonObject.put("auction_completion_date", timestamp.toString());


        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "CreateAuction");
        props.setContentType("text/plain");

        Message msg = new Message(jsonObject.toString().getBytes(), props);

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

    public void GetAuction(int auctionId){
        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "GetAuction");
        props.setContentType("text/plain");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("auctionId", auctionId);

        Message msg = new Message(jsonObject.toString().getBytes(), props);

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

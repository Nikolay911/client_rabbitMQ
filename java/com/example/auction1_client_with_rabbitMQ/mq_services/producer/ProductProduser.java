package com.example.auction1_client_with_rabbitMQ.mq_services.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ProductProduser {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GeneralProduser produser;

    @PreAuthorize("@customExpressions.isHisCustomer(#idCustomer, #request)")
    public void CreateProduct(int idCustomer, JsonNode body, HttpServletRequest request){

            MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
            props.setHeader("headerKey2", "CreateProduct");
            props.setContentType("text/plain");

            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = mapper.convertValue(body, JSONObject.class);
            jsonObject.put("id", idCustomer);

            com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.convertValue(jsonObject, JsonNode.class);

            Message msg = new Message(jsonNode.toString().getBytes(), props);
            rabbitTemplate.convertAndSend(produser.getQueue(), msg);
    }

    public void GetProduct(int productId) throws ParseException {

        MessageProperties props = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
        props.setHeader("headerKey2", "GetProduct");
        props.setContentType("text/plain");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", productId);

        Message msg = new Message(jsonObject.toString().getBytes(), props);
        rabbitTemplate.convertAndSend(produser.getQueue(), msg);

    }
}

package com.example.auction1_client_with_rabbitMQ.mq_services.consumer;


import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.*;


@Component
@Data
public class GeneralConsumer {

    private String str;

    @RabbitListener(queues = "${rabbitmq.queue2}")
    public void consume(String msg) {
        String filePath = "response.txt";
        String newMessange = "\n" + msg;

        System.out.println("mesage: " + newMessange);

        OutputStream os = null;
        try {

            os = new FileOutputStream(new File(filePath), true);
            os.write(newMessange.getBytes(), 0, newMessange.length());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

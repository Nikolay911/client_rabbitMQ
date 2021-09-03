package com.example.auction1_client_with_rabbitMQ.client_customerController;

import com.example.auction1_client_with_rabbitMQ.client_services.OrderService;
import com.example.auction1_client_with_rabbitMQ.mq_services.producer.OrderProduser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/client")
public class OrderController {

    private final OrderService orderService;
    private final OrderProduser produser;

    public OrderController(OrderService orderService, OrderProduser produser) {
        this.orderService = orderService;
        this.produser = produser;
    }


    @PostMapping("/bid/auction/{AuctionId}/customer/{CustomerId}")
    public String bid(@PathVariable int CustomerId,
                      @PathVariable int AuctionId,
                      @RequestBody JsonNode customerPrice) {
        produser.Bid(CustomerId, AuctionId, customerPrice);
        return "Запрос отправлен";
    }

}

package com.example.auction1_client_with_rabbitMQ.client_customerController;

import com.example.auction1_client_with_rabbitMQ.client_models.AuctionInformation;
import com.example.auction1_client_with_rabbitMQ.client_services.AuctionService;
import com.example.auction1_client_with_rabbitMQ.mq_services.producer.AuctionProduser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionProduser produser;

    public AuctionController(AuctionService auctionService, AuctionProduser produser) {
        this.auctionService = auctionService;
        this.produser = produser;
    }


    @PostMapping("/auction/customer/{customerId}/product/{productId}")
    public String createAuction(@PathVariable int customerId,
                                 @PathVariable int productId,
                                 @RequestBody JsonNode body){
        produser.CreateAuction(customerId, productId, body);
        return "Запрос отправлен";
    }

    @GetMapping("/auction/{auctionId}")
    public String getAuctionInformation(@PathVariable int auctionId) throws JsonProcessingException {
        produser.GetAuction(auctionId);
        return "Запрос отправлен";
    }

    @GetMapping("/auctions")
    public List<AuctionInformation> getAllAuctionsInformations() throws JsonProcessingException {
        return auctionService.getAllAuctionsInformation();
    }
}

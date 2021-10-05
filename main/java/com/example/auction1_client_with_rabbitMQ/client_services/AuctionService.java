package com.example.auction1_client_with_rabbitMQ.client_services;

import com.example.auction1_client_with_rabbitMQ.client_models.*;
import com.example.auction1_client_with_rabbitMQ.config.URLconfigProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Component
public class AuctionService extends RestTemplateService {

    private final URLconfigProperties urLconfigProperties;

    public AuctionService(URLconfigProperties urLconfigProperties) {
        this.urLconfigProperties = urLconfigProperties;
    }


    public Auction createAuction(int idCustomer, int idProduct, JsonNode body){

        Auction auction = restTemplate.postForObject(
                urLconfigProperties.getCreateAuction() + idCustomer + "/product/" + idProduct, body, Auction.class);
        return auction;
    }

    public AuctionInformation getAuctionInformation(int auctionId) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        Auction auction = restTemplate.getForObject(urLconfigProperties.getGetAuction() + auctionId, Auction.class);

        String orders = restTemplate.getForObject(urLconfigProperties.getGetAllAuctionBid() + auctionId, String.class);
        JsonNode jsonNode = objectMapper.readTree(orders);
        List<Order> allOrders = objectMapper.convertValue(jsonNode,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Order.class));

        if(auction.getAuctionCompletionDate().getTime() - new Timestamp(System.currentTimeMillis()).getTime() < 0){
            if(allOrders.size() == 0){
                auction.setApplicationStatus("Completed. Участники отсутствуют, победитель не определен.");
                return new AuctionInformation(auction, allOrders);
            }

            Double maxPrice = allOrders.stream().mapToDouble(x -> x.getCustomerPrice()).max().orElse(-1);
            List<Order> order = allOrders.stream().filter(x -> {
                if(abs(x.getCustomerPrice() - maxPrice) < 0.001)
                    return true;
                return false;
            }).collect(Collectors.toList());

            auction.setApplicationStatus("Completed. Победил участник с id = " + order.get(0).getId() + " назвав цену = " + order.get(0).getCustomerPrice());
            return new AuctionInformation(auction, order);
        }
        return new AuctionInformation(auction, allOrders);
    }

    public List<AuctionInformation> getAllAuctionsInformation() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<AuctionInformation> auctionInformations = new ArrayList<>();

        String auctions = restTemplate.getForObject(urLconfigProperties.getGetAllAuction(), String.class);
        JsonNode jsonNode = objectMapper.readTree(auctions);
        List<Auction> allOrders = objectMapper.convertValue(jsonNode,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Auction.class));

        int[] auctionsId = new int[allOrders.size()];

        for(int i = 0; i < allOrders.size(); i++){
            auctionsId[i] = allOrders.get(i).getId();
        }

        for (int j = 0; j<auctionsId.length; j++){
            auctionInformations.add(getAuctionInformation(auctionsId[j]));
        }
        return auctionInformations;
    }

}

package com.example.auction1_client_with_rabbitMQ.client_models;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class AuctionInformation {
    private int id;
    private int customerWhoCreatedAuction;
    private Timestamp auctionStartDate;
    private Timestamp auctionCompletionDate;
    private String applicationStatus;
    private List<Order> orders;

    public AuctionInformation(Auction auction, List<Order> orders){

        this.id = auction.getId();
        this.customerWhoCreatedAuction = auction.getCustomerWhoCreatedAuction();
        this.auctionStartDate = auction.getAuctionStartDate();
        this.auctionCompletionDate = auction.getAuctionCompletionDate();
        this.applicationStatus = auction.getApplicationStatus();
        this.orders = orders;
    }
}

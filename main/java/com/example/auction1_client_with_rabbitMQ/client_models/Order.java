package com.example.auction1_client_with_rabbitMQ.client_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int id;
    private double customerPrice;
    private Timestamp bidDate;

}


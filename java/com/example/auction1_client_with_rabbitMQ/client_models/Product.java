package com.example.auction1_client_with_rabbitMQ.client_models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Product {

    private int id;
    private File foto;
    private String path;
    private String productDescription;
    private double startPrice;

}



package com.example.auction1_client_with_rabbitMQ.client_models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Location {

    private int id;
    private String city;
    private String street;
    private String home;
    private int apartment;
    private String postCode;

    public Location(String city, String street, String home, int apartment, String postCode) {
        this.city = city;
        this.street = street;
        this.home = home;
        this.apartment = apartment;
        this.postCode = postCode;
    }
}
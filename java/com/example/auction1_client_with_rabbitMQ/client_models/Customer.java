package com.example.auction1_client_with_rabbitMQ.client_models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Customer {

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private java.sql.Date dateOfBirth;
    private List<Location> locations;

}


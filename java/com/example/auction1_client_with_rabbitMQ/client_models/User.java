package com.example.auction1_client_with_rabbitMQ.client_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String login;
    private String password;
    private String role;
    private int customerId;

    public User(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}

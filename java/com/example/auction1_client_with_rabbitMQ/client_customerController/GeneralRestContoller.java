package com.example.auction1_client_with_rabbitMQ.client_customerController;

import com.example.auction1_client_with_rabbitMQ.client_services.login.GeneralService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GeneralRestContoller {

    private GeneralService generalService;

    public GeneralRestContoller(GeneralService generalService) {
        this.generalService = generalService;
    }


    @GetMapping("/client/users")
    public JSONObject getUsers() {
        return generalService.getUsers();
    }

    @PostMapping("/user")
    public String postUser(@RequestBody JSONObject body) throws IOException {
        return generalService.postUser(body);
    }
}

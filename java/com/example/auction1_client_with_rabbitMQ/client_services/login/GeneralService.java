package com.example.auction1_client_with_rabbitMQ.client_services.login;

import com.example.auction1_client_with_rabbitMQ.client_models.User;
import com.example.auction1_client_with_rabbitMQ.config.LoginFileConfigProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GeneralService {

    private final PasswordEncoder passwordEncoder;
    private final LoginFileConfigProperties loginFileConfigProperties;

    public GeneralService(PasswordEncoder passwordEncoder, LoginFileConfigProperties loginFileConfigProperties) {
        this.passwordEncoder = passwordEncoder;
        this.loginFileConfigProperties = loginFileConfigProperties;
    }

    public JSONObject getUsers() {

        JSONParser parser = new JSONParser();
        JSONObject arrJson = null;

        try (FileReader reader = new FileReader(loginFileConfigProperties.getUsers())) {
            arrJson = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            System.out.println("Неправильный формат файла " + e.toString());
        }
        return arrJson;
    }

    public String postUser(JSONObject body) {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(body, User.class);

        if(findByUsername(user.getLogin()) != null){
            return "Пользователь с таким именем уже существует";
        }

        JSONArray jsonArray = (JSONArray) getUsers().get("users");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", user.getLogin());
        if(user.getCustomerId() == 0 && user.getPassword().length() < 60) {
            jsonObject.put("password", passwordEncoder.encode(user.getPassword()));
            jsonObject.put("role", "ROLE_user");
        }
        else {
            jsonObject.put("password", user.getPassword());
            jsonObject.put("role", user.getRole());
        }


        jsonObject.put("customerId", user.getCustomerId());

        jsonArray.add(jsonObject);

        JSONObject jsonName = new JSONObject();
        jsonName.put("users", jsonArray);

        File file = new File(loginFileConfigProperties.getUsers());

        try(PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(jsonName.toJSONString());
        } catch (Exception e) {
            System.out.println("Неправильный формат файла " + e.toString());
        }
        return "Пользователь создан.";
    }

    public User findByUsername(String username) {

        JSONArray jsonArray = (JSONArray) getUsers().get("users");

        ObjectMapper objectMapper = new ObjectMapper();
        List<User> user = objectMapper.convertValue(
                jsonArray, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class));

        List<User> userLog = user.stream().filter(x-> x.getLogin().equals(username)).collect(Collectors.toList());
        if(userLog.size()==1){
            User findUser = new User(
                    userLog.get(0).getLogin(), userLog.get(0).getPassword(), userLog.get(0).getRole(), userLog.get(0).getCustomerId());
            return findUser;
        }
        else{
            User badUser = null;
            return badUser;
        }
    }

    public User addCustomer(String username, int customerId) throws IOException {

        JSONArray jsonArray = (JSONArray) getUsers().get("users");
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> user = objectMapper.convertValue(jsonArray, objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, User.class));

        int index = IntStream.range(0, user.size()).filter(x -> user.get(x).getLogin().equals(username)).findFirst().orElse(-1);
        User newUser = findByUsername(username);
        newUser.setCustomerId(customerId);
        jsonArray.remove(index);

        JSONObject jsonUser = objectMapper.convertValue(newUser, JSONObject.class);
        jsonArray.add(jsonUser);

        File file = new File(loginFileConfigProperties.getUsers());
        file.delete();
        file.createNewFile();
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println("{\"users\": []}");
        printWriter.close();

        List<JSONObject> jsonObjects = (List<JSONObject>) jsonArray.stream().peek(x -> {
            postUser((JSONObject)x);
        }).collect(Collectors.toList());

        return newUser;
    }
}

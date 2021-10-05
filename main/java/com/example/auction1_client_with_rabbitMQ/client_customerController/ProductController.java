package com.example.auction1_client_with_rabbitMQ.client_customerController;

import com.example.auction1_client_with_rabbitMQ.client_services.ProductService;
import com.example.auction1_client_with_rabbitMQ.client_models.Product;
import com.example.auction1_client_with_rabbitMQ.mq_services.producer.ProductProduser;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/client")
public class ProductController {

    private final ProductService productService;
    private final ProductProduser productProduser;

    public ProductController(ProductService productService, ProductProduser productProduser) {
        this.productService = productService;
        this.productProduser = productProduser;
    }


    @PostMapping("/product/customer/{customerId}")
    public String createProduct(@PathVariable int customerId,
                                 @RequestBody JsonNode body, HttpServletRequest request){
        productProduser.CreateProduct(customerId, body, request);
        return "Запрос отправлен";
    }

    @GetMapping("/product/{productId}")
    public String getProduct(@PathVariable int productId) throws ParseException {

        productProduser.GetProduct(productId);

        return "Запрос отправлен";
    }

}

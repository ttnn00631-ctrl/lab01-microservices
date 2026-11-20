package com.example.orderservice;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

// Frontend gọi trực tiếp Order Service (cổng 8082) từ trình duyệt nên cũng cần bật CORS.
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final RestClient restClient;

    public OrderController(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    // GET /api/orders -> danh sách đơn hàng mẫu
    @GetMapping
    public List<Map<String, Object>> getOrders() {
        return List.of(
                Map.of("id", 101, "productId", 1, "quantity", 2),
                Map.of("id", 102, "productId", 2, "quantity", 1)
        );
    }

    // GET /api/orders/product/{id} -> Order Service gọi Product Service bằng RestClient
    @GetMapping("/product/{id}")
    public ResponseEntity<String> getProduct(@PathVariable int id) {
        try {
            String body = restClient.get()
                    .uri("/api/products/{id}", id)
                    .retrieve()
                    .body(String.class);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body);
        } catch (HttpClientErrorException.NotFound e) {
            // Product Service trả 404 -> chuyển tiếp 404 cho Client (Frontend hiển thị "Không tìm thấy sản phẩm")
            return ResponseEntity.notFound().build();
        }
    }
}

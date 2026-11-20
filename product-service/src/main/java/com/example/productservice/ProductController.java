package com.example.productservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// Cho phép Frontend (origin khác, ví dụ Live Server cổng 5500) gọi API.
// "*" chỉ nên dùng khi thực hành; khi triển khai thực tế cần giới hạn origin.
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Dữ liệu mẫu dùng chung cho cả hai API (tối thiểu 3 sản phẩm)
    private static final List<Map<String, Object>> PRODUCTS = List.of(
            Map.of("id", 1, "name", "Mouse", "price", 150000),
            Map.of("id", 2, "name", "Keyboard", "price", 250000),
            Map.of("id", 3, "name", "Monitor", "price", 2500000)
    );

    // GET /api/products -> danh sách sản phẩm
    @GetMapping
    public List<Map<String, Object>> getProducts() {
        return PRODUCTS;
    }

    // GET /api/products/{id} -> 200 OK nếu tìm thấy, 404 Not Found nếu không
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable int id) {
        return PRODUCTS.stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

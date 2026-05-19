package com.securemarket.controller;

import com.securemarket.dto.ProductRequestDTO;
import com.securemarket.entity.Product;
import com.securemarket.entity.User;
import com.securemarket.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products") // Rota: http://localhost:8080/products
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequestDTO requestDTO) {
        // 1. Pega o "VVIP" que está logado através do Token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();

        // 2. Chama o service para criar o produto atrelado à loja desse usuário
        Product newProduct = productService.createProduct(loggedInUser.getId(), requestDTO);

        // 3. Retorna o produto criado com status 201
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }
}
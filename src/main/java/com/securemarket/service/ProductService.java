package com.securemarket.service;

import com.securemarket.dto.ProductRequestDTO;
import com.securemarket.entity.Product;
import com.securemarket.entity.Seller;
import com.securemarket.repository.ProductRepository;
import com.securemarket.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    public Product createProduct(UUID userId, ProductRequestDTO dto) {
        // 1. Busca a loja (Seller) que pertence a este usuário logado
        // Se o cara tentar cadastrar produto sem ter loja, a gente barra!
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Você precisa criar uma loja antes de cadastrar produtos!"));

        // 2. Monta o produto amarrando à loja encontrada
        Product product = Product.builder()
                .seller(seller)
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .active(true)
                .build();

        // 3. Salva no banco de dados e devolve pronto
        return productRepository.save(product);
    }
}
package com.securemarket.controller;

import com.securemarket.dto.SellerRequestDTO;
import com.securemarket.entity.Seller;
import com.securemarket.entity.User;
import com.securemarket.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers") // Rota: http://localhost:8080/sellers
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody @Valid SellerRequestDTO requestDTO) {

        // ==========================================
        // O TRUQUE SÊNIOR: PEGANDO O ID DO TOKEN JWT
        // ==========================================
        // 1. Acessa o "bolso" do segurança para ver quem está logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Extrai o Usuário que está lá dentro
        User loggedInUser = (User) authentication.getPrincipal();

        // 3. Manda pro Service criar a loja passando o ID extraído automaticamente
        Seller newSeller = sellerService.createSeller(loggedInUser.getId(), requestDTO);

        // 4. Devolve a loja criada com Status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(newSeller);
    }
}
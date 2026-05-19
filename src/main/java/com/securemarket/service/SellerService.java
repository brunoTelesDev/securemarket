package com.securemarket.service;

import com.securemarket.dto.SellerRequestDTO;
import com.securemarket.entity.Seller;
import com.securemarket.entity.User;
import com.securemarket.repository.SellerRepository;
import com.securemarket.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service // Avisa o Spring que aqui fica a lógica de negócio
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public SellerService(SellerRepository sellerRepository, UserRepository userRepository) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
    }

    public Seller createSeller(UUID userId, SellerRequestDTO dto) {
        // 1. Busca o usuário dono da futura loja
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Cria o objeto Seller e amarra o usuário nele
        Seller seller = Seller.builder()
                .user(user)
                .cnpj(dto.cnpj())
                .storeName(dto.storeName())
                .description(dto.description())
                .build();

        // 3. Salva no banco de dados
        return sellerRepository.save(seller);
    }
}
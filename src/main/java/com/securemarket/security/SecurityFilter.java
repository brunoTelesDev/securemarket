package com.securemarket.security; // Ajuste o pacote se necessário

import com.securemarket.repository.UserRepository;
import com.securemarket.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Diz pro Spring: "Transforme isso num componente oficial do sistema"
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    // Esse método vai interceptar TODAS as requisições que chegarem na API
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Pega o Token do cabeçalho da requisição
        var token = this.recoverToken(request);

        if (token != null) {
            // 2. Manda a máquina ler. Se for válido, devolve o e-mail
            var login = tokenService.validateToken(token);

            if (!login.isEmpty()) {
                // 3. Acha o usuário no banco
                UserDetails user = userRepository.findByEmail(login).orElseThrow();

                // 4. Cria a "Credencial de Acesso" e avisa o Spring Security que o cara está logado!
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Manda a requisição seguir o fluxo normal pro Controller
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o Token da palavra "Bearer " (Padrão da internet)
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
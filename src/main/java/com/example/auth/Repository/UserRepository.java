package com.example.auth.Repository;

import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Repository
public class UserRepository {
    private WebClient webClient;
    private String apiKey;

    public UserRepository(WebClient.Builder webClientBuilder, @Value("${api.secret}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("http://host.docker.internal:8080/auth/api")
                .build();
        this.apiKey = apiKey;
    }
    public Mono<LoginResponseDto> findByUserId(String userId) {
        return this.webClient.get().uri("/{userId}", userId)
                .header("X-API-Key", this.apiKey)
                .retrieve()
                .bodyToMono(LoginResponseDto.class)
                .doOnSuccess(loginRequestDto -> {
                    System.out.println("API 요청 성공");
                })
                .doOnError(error -> {
                    System.err.println("API 요청 실패");
                    System.err.println("오류 메시지: " + error.getMessage());
                });
    }
}

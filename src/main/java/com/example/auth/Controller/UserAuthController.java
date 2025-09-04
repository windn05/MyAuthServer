package com.example.auth.Controller;



import com.example.auth.Service.AuthService;
import com.example.auth.dto.TokenResponseDto;
import com.example.auth.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserAuthController {
    private final AuthService authService;

    // 로그인 요청을 처리하고 JWT 토큰을 반환
    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponseDto>> login(@RequestBody LoginRequestDto dto) {
        // 로그인 성공 시 JWT 토큰을 받아서 응답 객체 생성
        // 로그인 실패 시 HTTP 상태 코드와 메시지 반환

        return authService.login(dto.getUserid(), dto.getPassword())
                .map(jwtToken -> {
                    TokenResponseDto responseDto = new TokenResponseDto(jwtToken,"success");
                    return ResponseEntity.ok(responseDto);
                })
                .onErrorResume(e -> {
                    TokenResponseDto errorResponse = new TokenResponseDto(null, "Invalid credentials");
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
                });
    }



}

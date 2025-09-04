package com.example.auth.Service;

import com.example.auth.JwtSecurity.JwtProvider;
import com.example.auth.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Mono<String> login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .flatMap(LoginResponseDto -> {
                    // 2. 가져온 해시와 입력된 비밀번호를 비교
                    if (passwordEncoder.matches(password, LoginResponseDto.getPasswordHash())) {
                        // 3. 비밀번호 일치 시, JWT 토큰 생성
                        return Mono.just(jwtTokenProvider.createToken(userId));
                    } else {
                        return Mono.error(new BadCredentialsException("유효하지 않은 인증정보입니다."));
                    }
                });
    }




}

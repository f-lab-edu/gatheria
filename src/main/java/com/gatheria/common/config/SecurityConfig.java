package com.gatheria.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index.html", "/static/**", "/assets/**",  // ✅ 정적 리소스 허용
                                "/login", "/register", "/api/auth/**", "/api/member/email-check" , "/api/member/student/register", "/api/member/instructor/register"// ✅ 로그인, 회원가입 API 허용
                        ).permitAll()
                        .requestMatchers("/dashboard/**").authenticated() // ✅ 대시보드는 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ 세션 사용 안 함 (JWT)
                .formLogin(form -> form.disable()) // ✅ 기본 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // ✅ HTTP Basic 인증 비활성화
                .logout(logout -> logout.logoutUrl("/api/auth/logout").permitAll()); // ✅ 로그아웃 API 추가

        return http.build();
    }
}

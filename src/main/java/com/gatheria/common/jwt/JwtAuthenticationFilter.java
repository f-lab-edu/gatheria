package com.gatheria.common.jwt;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateAccessToken(token)) {
                String email = jwtUtil.extractEmail(token);

                MemberRole role = MemberRole.valueOf(jwtUtil.extractRole(token));
                Long memberId = jwtUtil.extractMemberId(token);

                // SecurityContext에 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(role.name()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);


                AuthInfo authInfo = new AuthInfo(email, role, memberId);

                request.setAttribute("authInfo", authInfo);

                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        List<String> excludedPaths = List.of(
                "/api/auth/",
                "/",
                "/api/public/",
                "/index.html",
                "/static/**",
                "/assets/**",
                "/admin",
                "/login",
                "/register",
                "/api/member/email-check",
                "/api/member/student/register",
                "/api/member/instructor/register"
        );

        return excludedPaths.stream()
                .anyMatch(path::startsWith);
    }
}

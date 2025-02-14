package com.gatheria.common.jwt;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        System.out.println("✅ JwtAuthenticationFilter 등록 완료!");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("🔍 JwtAuthenticationFilter 실행됨!");


        String authHeader = request.getHeader("Authorization");
        System.out.println("!!!!AuthHeader: " + authHeader);

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("!!!! Extracted Token: " + token);

            if (jwtUtil.validateAccessToken(token)) {
                String email = jwtUtil.extractEmail(token);
                MemberRole role = MemberRole.valueOf(jwtUtil.extractRole(token));
                Long memberId = jwtUtil.extractMemberId(token);


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(role.name()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);


                AuthInfo authInfo = new AuthInfo(email, role, memberId);

                request.setAttribute("authInfo", authInfo);

                System.out.println("✅ SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());

                filterChain.doFilter(request, response);
                return;
            } else {
                System.out.println("❌ Invalid JWT Token");
                SecurityContextHolder.clearContext();
            }
        }else {
            System.out.println("❌ No Authorization Header");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        List<String> excludedPaths = List.of(
                "/api/auth/",
                "/",
                "/api/public/",
                "/index.html",
                "/static/",
                "/assets/",
                "/vite.svg",
                "/admin",
                "/login",
                "/register",
                "/api/member/email-check",
                "/api/member/student/register",
                "/api/member/instructor/register"
        );

        return excludedPaths.stream().anyMatch(path::startsWith);
    }



}

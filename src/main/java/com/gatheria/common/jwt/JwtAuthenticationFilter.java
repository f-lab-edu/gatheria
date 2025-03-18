package com.gatheria.common.jwt;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter implements Filter {

  private final JwtUtil jwtUtil;

  private static final List<String> onlyForGuestPaths = List.of(
      "/api/auth/login",
      "/api/auth/register"
  );

  private static final List<String> alwaysAccessiblePaths = List.of(
      "/index.html",
      "/assets/",
      "/static/"
  );

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if (shouldNotFilter(httpRequest)) {
      if (request.getAttribute("authInfo") != null && onlyForGuestPaths.contains(
          httpRequest.getServletPath())) {
        httpResponse.sendRedirect("/dashboard");
        return;
      }
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = httpRequest.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      if (jwtUtil.validateAccessToken(token)) {
        String email = jwtUtil.extractEmail(token);
        MemberRole role = MemberRole.fromString(jwtUtil.extractRole(token));
        Long memberId = jwtUtil.extractMemberId(token);

        Long studentId = null;
        Long instructorId = null;

        if (role == MemberRole.STUDENT) {
          studentId = jwtUtil.extractStudentId(token);
        } else if (role == MemberRole.INSTRUCTOR) {
          instructorId = jwtUtil.extractInstructorId(token);
        }

        AuthInfo authInfo = new AuthInfo(email, role, memberId, studentId, instructorId);

        httpRequest.setAttribute("authInfo", authInfo);

        filterChain.doFilter(request, response);
        return;
      } else {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean shouldNotFilter(HttpServletRequest request) {

    String path = request.getServletPath();

    if (alwaysAccessiblePaths.contains(path)) {
      return true;
    }
    return onlyForGuestPaths.contains(path) && request.getAttribute("authInfo") != null;
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}

package com.test.noverina.transaction.config;

import com.test.noverina.transaction.repository.UserRepository;
import com.test.noverina.transaction.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository repo;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        var path = req.getRequestURI();
        return req.getMethod().equalsIgnoreCase("OPTIONS")
                || path.startsWith("/auth")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
            throws ServletException, IOException {
        var headerToken = req.getHeader("Authorization");
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        var token = headerToken.substring(7);
        try {
            if (!token.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                var auth = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("Unauthorized - Invalid JWT {}", e.getMessage());
            return;
        } catch (AccessDeniedException ae) {
            SecurityContextHolder.clearContext();
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            log.warn("Forbidden - Invalid JWT {}", ae.getMessage());
            return;
        }

        chain.doFilter(req, res);
    }

}

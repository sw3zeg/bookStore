package com.example.bookstore.app.filters;

import com.example.bookstore.app.model.exception.AppError;
import com.example.bookstore.app.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;


public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    private static final RequestMatcher PRIVATE_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/private/**"),
            new AntPathRequestMatcher("/admin/**")
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (PRIVATE_URLS.matches(request)) {
            String authHeader = request.getHeader("Authorization");
            String username = null;
            String jwtToken = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
                try {
                    username = jwtTokenService.extractUsername(jwtToken);

                } catch (ExpiredJwtException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(
                            new ObjectMapper().writeValueAsString(
                                    new AppError(
                                            HttpStatus.UNAUTHORIZED.value(),
                                            "Token lifetime out"
                                    )));
                    return;
                } catch (MalformedJwtException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(
                            new ObjectMapper().writeValueAsString(
                                    new AppError(
                                            HttpStatus.UNAUTHORIZED.value(),
                                            "Invalid signing"
                                    )));
                    return;
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtTokenService.extractRoles(jwtToken)
                                    .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                    SecurityContextHolder.getContext().setAuthentication(token);
                }

            }
        }

        filterChain.doFilter(request, response);
    }
}

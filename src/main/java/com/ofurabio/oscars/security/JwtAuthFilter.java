package com.ofurabio.oscars.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);

                if (token != null && !token.isEmpty()) {
                    username = jwtService.extractUsername(token);
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: " + e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Token expirado");
        } catch (UnsupportedJwtException e) {
            logger.error("Token não suportado: " + e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Token não suportado");
        } catch (MalformedJwtException e) {
            logger.error("Token malformado: " + e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Token malformado");
        } catch (SignatureException e) {
            logger.error("Assinatura inválida: " + e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Assinatura inválida");
        } catch (Exception e) {
            logger.error("Erro ao processar o token: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Erro ao processar o token");
        }
    }
}

package com.mookaps.cms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mookaps.cms.files.JsonFileService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService service;
    @Autowired
    private JsonFileService jService;

    // private static final List<String> PUBLIC_PATHS = List.of("/",
    // "/auth/authenticate", "/docs");

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;

        // String path = httpServletRequest.getRequestURI();
        // if (PUBLIC_PATHS.contains(path)) {
        // filterChain.doFilter(httpServletRequest, httpServletResponse);
        // return;
        // }

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            // userName = jwtUtil.extractUsername(token);

            List<Map<String, Object>> existingList = jService.readJson();
            for (int i = 0; i < existingList.size(); i++) {
                String saved = existingList.get(i).get("token").toString();
                if (saved.equals(token)) {
                    sendError(httpServletResponse, "Token has been invalidate");
                    return;
                }
            }

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtUtil.secret)
                        .parseClaimsJws(token)
                        .getBody();

                userName = claims.getSubject();
            } catch (ExpiredJwtException e) {
                sendError(httpServletResponse, "JWT expired");
                return;
            } catch (SignatureException e) {
                sendError(httpServletResponse, "Invalid JWT signature");
                return;
            } catch (MalformedJwtException | IllegalArgumentException e) {
                sendError(httpServletResponse, "Malformed JWT token");
                return;
            }
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(userName);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":401,\"message\":\"" + message + "\",\"count\":0,\"data\":[]}");
    }
}

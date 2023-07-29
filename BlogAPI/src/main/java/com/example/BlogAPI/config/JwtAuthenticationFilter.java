package com.example.BlogAPI.config;

import com.example.BlogAPI.helper.Constants;
import com.example.BlogAPI.helper.JwtUtils;
import com.example.BlogAPI.models.User;
import com.example.BlogAPI.services.UserAuthDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    User user;
    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
    ZoneId zoneId = ZoneId.systemDefault();

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserAuthDetailService userAuthDetailService;

    public void responseMessage(HttpServletResponse response, String message, String path) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        map.put("statusCode", String.valueOf(HttpStatus.FORBIDDEN));
        map.put("timeStamp", String.valueOf(LocalDateTime.now().atZone(zoneId).toEpochSecond()));
        map.put("message", message);
        new ObjectMapper().writeValue(response.getOutputStream(), map);

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader(Constants.AUTHORIZATION);
        String email = null;
        String jwtToken;

        try {
            if (requestTokenHeader != null && requestTokenHeader.startsWith(Constants.BEARER)) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    email = this.jwtUtils.extractUserName(jwtToken);
                    logger.info("User found with email: " + email);
                } catch (Exception e) {
                    logger.error(Constants.INVALID_TOKEN);
                    responseMessage(response, Constants.INVALID_TOKEN, request.getServletPath());
                }

                UserDetails userDetails = this.userAuthDetailService.loadUserByUsername(email);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("User authentication successful.");
                } else {
                    logger.error("User not authenticated");
                    responseMessage(response, "User not authenticated", request.getServletPath());
                }

            }
        } catch (Exception e) {
            logger.error(Constants.INVALID_TOKEN);
            responseMessage(response, e.getMessage(), request.getServletPath());
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Filter chain error: " + e.getMessage());
        }
    }
}

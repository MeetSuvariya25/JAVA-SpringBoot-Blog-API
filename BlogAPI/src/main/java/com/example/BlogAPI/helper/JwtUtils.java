package com.example.BlogAPI.helper;

import com.example.BlogAPI.exceptions.UserNotFoundException;
import com.example.BlogAPI.models.User;
import com.example.BlogAPI.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Autowired
    private UserRepository userRepository;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(Constants.SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Map<String, String> generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", createToken(claims, userDetails.getUsername()));
        tokens.put("refresh_token", createRefreshToken(userDetails.getUsername()));
        return tokens;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 120))
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY)
                .compact();
    }

    private String createRefreshToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 180))
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY)
                .compact();
    }

    public Boolean validateRefreshToken(String token) {
        return (!isTokenExpired(token));
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND));
    }

    public User findUserFromToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.AUTHORIZATION).split(" ")[1];
        try {
            String email = extractUserName(token);
            return findByEmail(email);
        } catch (Exception exception) {
            throw new UserNotFoundException();
        }
    }


}

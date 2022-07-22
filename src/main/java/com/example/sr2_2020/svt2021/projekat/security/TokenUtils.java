package com.example.sr2_2020.svt2021.projekat.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class TokenUtils {

    @Value("secret")
    private String secret;

    @Value("3600000")
    private Long expiration;

    public String getUsernameFromToken(String token) {

        try {

            Map<String, Object> claimsList = new HashMap<>(this.getClaimsFromToken(token));

            return claimsList.get("username").toString();

        } catch (Exception e) {

            return null;

        }
    }

    public String getUserRoleFromToken(String token) {

        try {

            Map<String, Object> claimsList = new HashMap<>(this.getClaimsFromToken(token));

            return claimsList.get("role").toString();

        } catch (Exception e) {

            return null;
        }
    }

    public Claims getClaimsFromToken(String token) {

        Claims claims;

        try {

            claims = Jwts.parser().setSigningKey(this.secret)
                    .parseClaimsJws(token).getBody();

        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }

    public Date getExpirationDateFromToken(String token) {

        Date expirationDate;

        try {

            final Claims claims = this.getClaimsFromToken(token);
            expirationDate = claims.getExpiration();

        } catch (Exception e) {

            expirationDate = null;
        }
        return expirationDate;
    }

    private boolean isTokenExpired(String token) {

        final Date expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));

    }

    public boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("username", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().toArray()[0]);
        claims.put("created", new Date(System.currentTimeMillis()));

        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public int getExpiredIn() {

        return expiration.intValue();
    }

    public String getToken( HttpServletRequest request ) {

        String authHeader = request.getHeader("Authorization");

        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

}

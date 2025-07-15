package com.wildan.e_commerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "Boayq98Alz0NzFSJuTk7qyY/ErswyS7Kz+Tpk7Gr03KvY7ehM3FMF+W6PMPV+R5G5uvvVmVvcHTg4Pc0TbB/yiDhUJjANGfBIShSiMUW3U+0tqHAZcsQy6TekEy+CXCFjddzM3AYttbJgdjAWZn+RQC+exzhYa3LvsftAi0bcZSfYgtF4MiJ4yTtLLotE+AEjAYec0uM1nF0ahI5AdnvGi5j4Ko6b57euCB0n98LENhJ8sG3KBlgSvCcy/BMfZd8+rMY+iLrZvTd54N+s4aArP0nXi4eVmobuIuqBUqXz6K7hAo8JaqELgr08dYpQFMp+2krNRvsSAEmfhS2kpfyTKe2RWXoT9eMpIO61esK/o8=";

    public String extractUsername(String token){
        return extractClaims(token , Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims , T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }

    public String generateToken(
            Map<String , Objects> extractClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7000 * 6  * 24))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUsername(token);
        return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token , Claims::getExpiration);
    }


}

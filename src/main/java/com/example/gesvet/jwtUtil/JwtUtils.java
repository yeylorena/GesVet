package com.example.gesvet.jwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import javax.crypto.SecretKey;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class JwtUtils {

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final String ISSUER = "coding_streams_auth_server";

    private JwtUtils() {
    }

    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken)
                    .getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT Exception occurred");
        }

        return Optional.empty();
    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken);
        return claimsOptional.map(Claims::getSubject);
    }

    public static String generateToken(String username) {
        var currentDate = new Date();
        var jwtExpirationInMinutes = 10; // Ajustar según sea necesario

        // Calcular la fecha de vencimiento usando Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MINUTE, jwtExpirationInMinutes);
        Date expiration = calendar.getTime();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();

    }

    public static Claims extractClaims(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            return jwtParser.parseClaimsJws(jwtToken).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT Exception occurred");
        }

        return null;
    }
}

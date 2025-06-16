package sideproject.gugumo.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sideproject.gugumo.domain.dto.memberDto.LoginCreateJwtDto;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class JwtUtil {

    // JWT에서는 String 키를 사용하는 방식에서 SecretKey라는 객체를 키로 사용하는 방식으로 변경됨.
    private final SecretKey secretKey;
    private Long expiredMs;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.expiration_time}") String expiredMs) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiredMs = Long.parseLong(expiredMs);
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(LoginCreateJwtDto loginCreateJwtDto) {

        Date requestDate = Timestamp.valueOf(loginCreateJwtDto.getRequestTimeMs());
        Date expireDate = Timestamp.valueOf(loginCreateJwtDto.getRequestTimeMs().plusSeconds(expiredMs / 1000));

        return Jwts.builder()
                .claim("id", loginCreateJwtDto.getId())
                .claim("username", loginCreateJwtDto.getUsername())
                .claim("role", loginCreateJwtDto.getRole())
                .issuedAt(requestDate)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }
}

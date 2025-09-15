package info.skantuz.personal_bank.jwt;

import info.skantuz.personal_bank.port.out.AuthValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

/**
 * Service for handling JWT token validation and generation.
 * Implements {@link AuthValidation} for authentication operations.
 */
@Service
public class JwtService implements AuthValidation {

  private final Key secretKey;

  /**
   * Constructs a JwtService with the provided secret key.
   * @param secretKey the secret key used for signing and validating JWT tokens
   */
  public JwtService(@Value("${app.jwt.secret}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  /**
   * Validates a JWT token and extracts the subject (user identifier).
   * @param token the JWT token to validate
   * @return a {@link Mono} emitting the subject if the token is valid
   */
  @Override
  public Mono<String> validateToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return Mono.just(claims.getSubject());
  }

  /**
   * Validates user credentials and generates a JWT token if valid.
   * @param userId the user identifier
   * @param password the user's password
   * @return a {@link Mono} emitting the JWT token if credentials are valid, empty otherwise
   */
  @Override
  public Mono<String> validateUser(String userId, String password) {
    if(userId.equals("admin") && password.equals("admin")) {
      Claims claims = Jwts.claims().setSubject(userId);
      return Mono.just(Jwts.builder()
          .setClaims(claims)
          .signWith(secretKey)
          .compact());
    }

    return Mono.empty();
  }
}
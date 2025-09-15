package info.skantuz.personal_bank.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class JwtServiceTest {

  private JwtService jwtService;
  private final String secret = "my-very-secret-key-which-is-32bytes!";

  @BeforeEach
  void setUp() {
    jwtService = new JwtService(secret);
  }

  @Test
  void validateUser_shouldReturnToken_whenCredentialsAreValid() {
    StepVerifier.create(jwtService.validateUser("admin", "admin"))
        .expectNextMatches(token -> token != null && !token.isEmpty())
        .verifyComplete();
  }

  @Test
  void validateUser_shouldReturnEmpty_whenCredentialsAreInvalid() {
    StepVerifier.create(jwtService.validateUser("user", "wrong"))
        .verifyComplete();
  }

  @Test
  void validateToken_shouldReturnSubject_whenTokenIsValid() {
    String token = jwtService.validateUser("admin", "admin").block();
    StepVerifier.create(jwtService.validateToken(token))
        .expectNext("admin")
        .verifyComplete();
  }
}
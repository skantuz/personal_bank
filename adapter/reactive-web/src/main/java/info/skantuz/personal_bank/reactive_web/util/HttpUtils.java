package info.skantuz.personal_bank.reactive_web.util;

import info.skantuz.personal_bank.model.error.ErrorList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class HttpUtils {

  private final String authHeader;

  public HttpUtils(@Value("${app.auth-header}") String authHeader) {
    this.authHeader = authHeader;
  }

  public Mono<String> validateToken(MultiValueMap<String, String> header) {
    if (Objects.isNull(header.get(authHeader))) {
      throw ErrorList.FORBIDDEN_ACTION.toApiException();
    }
    return Mono.just(header.get(authHeader).getFirst());
  }
}

package info.skantuz.personal_bank.reactive_web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.skantuz.personal_bank.model.error.ApiException;
import info.skantuz.personal_bank.reactive_web.config.pojo_error.Errors;
import info.skantuz.personal_bank.reactive_web.config.pojo_error.HttpError;
import info.skantuz.personal_bank.reactive_web.config.pojo_error.Meta;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {


  private final ObjectMapper mapper;
  DataBuffer dataBuffer = null;

  @SneakyThrows
  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    ServerHttpRequest request = exchange.getRequest();
    Map<String, Object> map = new HashMap<>();
    Meta meta = Meta.builder()
        ._clientRequest(request.getId())
        ._requestDate(LocalDateTime.now())
        ._messageId(UUID.randomUUID())
        ._version("1.0")
        .additionalProp1(mapper.readTree(request.toString()))
        .build();
    try {
      throw ex;
    } catch (ApiException exception){
      DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
      exchange.getResponse().setStatusCode(HttpStatus.valueOf(exception.getStatusCode()));
      Errors errors = Errors.builder()
          .status(exception.getStatusCode())
          .code(exception.getCode())
          .title(exception.getTitle())
          .detail(exception.getMessage())
          .id(meta.get_messageId())
          .build();
      dataBuffer = bufferFactory.wrap(mapper.writeValueAsBytes(
          HttpError.builder().meta(meta).errors(errors).build()));
      log.info(meta.get_messageId() + " Request: " + exchange.getRequest().toString());
      log.info(meta.get_messageId() + " Error: " + errors.getDetail());
    }

    return exchange.getResponse().writeWith(Mono.just(dataBuffer));
  }
}

package info.skantuz.personal_bank.reactive_web.config.pojo_error;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class HttpError {

  private final Meta meta;
  private final Errors errors;

}
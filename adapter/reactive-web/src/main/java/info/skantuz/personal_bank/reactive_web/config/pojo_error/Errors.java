package info.skantuz.personal_bank.reactive_web.config.pojo_error;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Errors {
  private final UUID id;
  private final String href;
  private final Integer status;
  private final String code;
  private final String title;
  private final String detail;
}


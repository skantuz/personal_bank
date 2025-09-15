package info.skantuz.personal_bank.reactive_web.config.pojo_error;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Meta {
  private final UUID _messageId;
  private final String _version;
  private final LocalDateTime _requestDate;
  private final String _clientRequest;
  private final JsonNode additionalProp1;
}

package info.skantuz.personal_bank.model.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorListTest {

    @Test
    void testAllEnumValuesToApiException() {
        for (ErrorList error : ErrorList.values()) {
            String log = "test log";
            ApiException ex = error.toApiException(log);
            assertNotNull(ex);
            assertEquals(error.toApiException().getStatusCode(), ex.getStatusCode());
            assertEquals(error.toApiException().getCode(), ex.getCode());
            assertEquals(error.toApiException().getTitle(), ex.getTitle());
            assertEquals(error.toApiException().getDescription(), ex.getDescription());
            assertEquals(log, ex.getLog());
        }
    }
}
package info.skantuz.personal_bank.model.error;

/**
 * Enum representing the list of possible error types in the personal bank domain.
 * Each error contains an HTTP status code, a unique code, a title, and a description.
 */
public enum ErrorList {
  CUSTOMER_ALREADY_EXISTS(409, "CUST_001", "Customer Already Exists", "A customer with the given details already exists."),
  CUSTOMER_NOT_FOUND(404, "CUST_002", "Customer Not Found", "The specified customer does not exist."),
  ACCOUNT_NOT_FOUND(404, "ACC_001", "Account Not Found", "The specified account does not exist."),
  ACCOUNT_NUMBER_ALLOWED(400, "ACC_005", "Account Number Required empty", "The account number is required in empty or null."),
  INSUFFICIENT_FUNDS(400, "ACC_002", "Insufficient Funds", "The account does not have enough balance."),
  INVALID_ACCOUNT_STATE(400, "ACC_003", "Invalid Account State", "The account state is invalid for this operation."),
  TRANSACTION_LIMIT_EXCEEDED(400, "ACC_004", "Transaction Limit Exceeded", "You have exceeded your transaction limit."),
  UNAUTHORIZED_ACCESS(401, "AUTH_001", "Unauthorized Access", "You are not authorized to perform this action."),
  DUPLICATE_TRANSACTION(409, "TXN_001", "Duplicate Transaction", "This transaction has already been processed."),
  CURRENCY_MISMATCH(400, "CUR_001", "Currency Mismatch", "The currencies do not match."),
  INVALID_AMOUNT(400, "AMT_001", "Invalid Amount", "The amount specified is invalid."),
  NETWORK_ERROR(503, "NET_001", "Network Error", "A network error occurred."),
  DATABASE_ERROR(500, "DB_001", "Database Error", "A database error occurred."),
  EXTERNAL_SERVICE_FAILURE(502, "EXT_001", "External Service Failure", "An external service failed."),
  UNKNOWN_ERROR(500, "GEN_001", "Unknown Error", "An unknown error occurred.");

  private final int statusCode;
  private final String code;
  private final String title;
  private final String description;

  /**
   * Constructor for ErrorList enum.
   *
   * @param statusCode  HTTP status code associated with the error
   * @param code        Unique error code
   * @param title       Short title for the error
   * @param description Detailed description of the error
   */
  ErrorList(int statusCode, String code, String title, String description) {
    this.statusCode = statusCode;
    this.code = code;
    this.title = title;
    this.description = description;
  }

  /**
   * Converts this error to an ApiException, including a log message.
   *
   * @param log Additional log message for debugging
   * @return ApiException instance representing this error
   */
  public ApiException toApiException(String log) {
    return new ApiException(statusCode, code, title, description, log);
  }

  /**
   * Converts this error to an ApiException without a log message.
   *
   * @return ApiException instance representing this error
   */
  public ApiException toApiException() {
    return new ApiException(statusCode, code, title, description, null);
  }
}
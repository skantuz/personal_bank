package info.skantuz.personal_bank.port;

public interface AuthValidation {
    boolean validateToken(String token);
    boolean validateUser(String userId, String password);
}

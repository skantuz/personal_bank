package info.skantuz.model;

import java.util.Date;

public record Customer(String id, String identificationType, String identificationNumber, String name, String lastname,
                       String email, Date birthdate, Date) {
}

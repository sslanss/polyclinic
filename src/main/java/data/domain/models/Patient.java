package data.domain.models;

import data.domain.models.dictionaries.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Patient {
    private String insurancePolicyNumber;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private String password;
}

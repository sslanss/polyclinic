package data.dto;

import data.domain.models.dictionaries.Gender;
import java.time.LocalDate;

public record PatientProfileDto(String insurancePolicyNumber, String fullName, LocalDate dateOfBirth,
                               Gender gender, String phoneNumber, String address) {
}

package data.domain.models;

import data.domain.models.dictionaries.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Doctor {
    private Integer doctorId;
    private Integer specialityId;
    private String fullName;
    private Gender gender;
    private String phoneNumber;

    public Doctor(Integer specialityId, String fullName, Gender gender, String phoneNumber) {
        this.specialityId = specialityId;
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
}

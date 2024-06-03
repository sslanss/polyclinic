package data.domain.models;

import data.domain.models.dictionaries.Gender;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctor)) {
            return false;
        }
        Doctor doctor = (Doctor) o;
        return Objects.equals(specialityId, doctor.specialityId)
                 && Objects.equals(fullName, doctor.fullName)
                 && gender == doctor.gender
                 && Objects.equals(phoneNumber, doctor.phoneNumber);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((specialityId == null) ? 0 : specialityId.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        return result;
    }
}

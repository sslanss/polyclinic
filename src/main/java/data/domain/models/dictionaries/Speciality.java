package data.domain.models.dictionaries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Speciality {
    private Integer specialityId;
    private String specialityName;

    public Speciality(String specialityName) {
        this.specialityName = specialityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Speciality speciality = (Speciality) o;
        return Objects.equals(specialityName, speciality.specialityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialityName);
    }
}

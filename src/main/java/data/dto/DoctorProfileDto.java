package data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorProfileDto {
    private Integer doctorId;
    private String fullName;
    private String speciality;
}

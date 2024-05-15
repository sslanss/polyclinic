package data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorListItemDto {
    private Integer doctorId;
    private String doctorFullName;
}

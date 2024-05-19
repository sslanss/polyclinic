package data.domain.models;

import data.domain.models.dictionaries.AppointmentType;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatientRecord {
    private Integer recordId;
    private Integer doctorId;
    private String patientId;
    private OffsetDateTime dateTime;
    private AppointmentType appointmentType;
}

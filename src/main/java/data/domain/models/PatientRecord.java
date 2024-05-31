package data.domain.models;

import data.domain.models.dictionaries.AppointmentType;
import java.time.LocalDateTime;
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
    private LocalDateTime dateTime;
    private AppointmentType appointmentType;
    private String doctorFullName;

    public PatientRecord(Integer recordId, Integer doctorId, String patientId, LocalDateTime dateTime,
                         AppointmentType appointmentType) {
        this.recordId = recordId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.dateTime = dateTime;
        this.appointmentType = appointmentType;
    }

    public PatientRecord(Integer doctorId, String patientId, LocalDateTime dateTime,
                         AppointmentType appointmentType) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.dateTime = dateTime;
        this.appointmentType = appointmentType;
    }

}

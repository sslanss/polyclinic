package data.domain.models;

import data.domain.models.dictionaries.AppointmentType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecord that = (PatientRecord) o;
        return Objects.equals(doctorId, that.doctorId)
               && Objects.equals(patientId, that.patientId)
                && Objects.equals(dateTime.truncatedTo(ChronoUnit.SECONDS),
                        that.dateTime.truncatedTo(ChronoUnit.SECONDS))
                && appointmentType == that.appointmentType;
    }

    @Override
    public int hashCode() {
        int result = doctorId != null ? doctorId.hashCode() : 0;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (appointmentType != null ? appointmentType.hashCode() : 0);
        return result;
    }
}

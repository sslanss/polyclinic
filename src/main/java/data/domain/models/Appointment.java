package data.domain.models;

import data.domain.models.dictionaries.AppointmentType;
import java.time.OffsetDateTime;

public class Appointment {
    private Integer appointmentId;
    private Integer doctorId;
    private String patientId;
    private OffsetDateTime dateTime;
    private String recommendations;
    private AppointmentType appointmentType;
}

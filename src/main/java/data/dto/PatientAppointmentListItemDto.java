package data.dto;

import java.time.LocalDateTime;

public record PatientAppointmentListItemDto(Integer recordId, String doctorFullName,
                                            LocalDateTime dateTime) {
}

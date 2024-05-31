package data.dto;

import java.time.LocalDateTime;

public record PatientRecordResultDto(String doctorFullName, LocalDateTime dateTime) {
}

package data.dto;

import java.time.LocalDateTime;

public record AddedPatientRecordDto(String doctorFullName, LocalDateTime dateTime) {
}

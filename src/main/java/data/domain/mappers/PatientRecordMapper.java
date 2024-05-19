package data.domain.mappers;

import data.domain.models.PatientRecord;
import data.dto.AddedPatientRecordDto;
import data.dto.DoctorFreeTimeForRecordDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PatientRecordMapper {

    private final AppointmentTypeMapper appointmentTypeMapper;

    public PatientRecord mapPatientRecordFromDatabase(ResultSet resultSet) throws SQLException {
        return new PatientRecord(
                resultSet.getInt("record_id"),
                resultSet.getInt("doctor_id"),
                resultSet.getString("patient_id"),
                resultSet.getObject("date_time", LocalDateTime.class),
                appointmentTypeMapper.mapAppointmentTypeFromDatabase(resultSet)
        );
    }

    public DoctorFreeTimeForRecordDto mapPatientRecordToRecordsToDoctorListDto(PatientRecord patientRecord) {
        return new DoctorFreeTimeForRecordDto(patientRecord.getDateTime());
    }

    public AddedPatientRecordDto mapPatientRecordToAddedPatientRecordDto(PatientRecord patientRecord,
                                                                         String doctorFullName) {
        return new AddedPatientRecordDto(doctorFullName, patientRecord.getDateTime());
    }
}

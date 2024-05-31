package data.domain.mappers;

import data.domain.models.PatientRecord;
import data.dto.DoctorAvailableTimeDto;
import data.dto.PatientAppointmentListItemDto;
import data.dto.PatientRecordResultDto;
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

    public PatientRecord mapPatientRecordWithDoctorFullNameFromDatabase(ResultSet resultSet) throws SQLException {
        PatientRecord patientRecord  = mapPatientRecordFromDatabase(resultSet);
        patientRecord.setDoctorFullName(resultSet.getString("doctor_full_name"));
        return patientRecord;
    }

    public DoctorAvailableTimeDto mapPatientRecordToRecordsToDoctorListDto(PatientRecord patientRecord) {
        return new DoctorAvailableTimeDto(patientRecord.getDateTime());
    }

    public PatientRecordResultDto mapPatientRecordToPatientRecordResultDto(PatientRecord patientRecord,
                                                                           String doctorFullName) {
        return new PatientRecordResultDto(doctorFullName, patientRecord.getDateTime());
    }

    public PatientAppointmentListItemDto mapPatientRecordToPatientAppointmentListItemDto(PatientRecord patientRecord) {
        return new PatientAppointmentListItemDto(patientRecord.getRecordId(), patientRecord.getDoctorFullName(),
                patientRecord.getDateTime());
    }
}

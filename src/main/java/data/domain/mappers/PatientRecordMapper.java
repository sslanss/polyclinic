package data.domain.mappers;

import data.domain.models.PatientRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
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
                resultSet.getObject("date_time", OffsetDateTime.class),
                appointmentTypeMapper.mapAppointmentTypeFromDatabase(resultSet)
        );
    }
}

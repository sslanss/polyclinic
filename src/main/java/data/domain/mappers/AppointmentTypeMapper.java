package data.domain.mappers;

import data.domain.models.dictionaries.AppointmentType;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentTypeMapper {
    public AppointmentType mapAppointmentTypeFromDatabase(ResultSet resultSet) throws SQLException {
        String appointmentTypeString = resultSet.getString("appointment_type");
        return AppointmentType.valueOf(appointmentTypeString.toUpperCase());
    }
}

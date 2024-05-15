package data.domain.mappers;

import data.domain.ClassMappingException;
import data.domain.models.Patient;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientMapper {
    private final GenderMapper genderMapper;

    public PatientMapper(GenderMapper genderMapper) {
        this.genderMapper = genderMapper;
    }

    public Patient mapPatientFromDatabase(ResultSet resultSet) {
        try {
            return new Patient(
                    resultSet.getString("insurance_policy_number"),
                    resultSet.getString("full_name"),
                    resultSet.getDate("date_of_birth").toLocalDate(),
                    genderMapper.mapGenderFromDatabase(resultSet),
                    resultSet.getString("phone_number"),
                    resultSet.getString("address")
            );
        } catch (SQLException e) {
            throw new ClassMappingException();
        }
    }
}

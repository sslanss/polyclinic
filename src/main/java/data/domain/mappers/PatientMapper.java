package data.domain.mappers;

import data.domain.models.Patient;
import data.dto.PatientProfileDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientMapper {
    private final GenderMapper genderMapper;

    public PatientMapper(GenderMapper genderMapper) {
        this.genderMapper = genderMapper;
    }

    public Patient mapPatientFromDatabase(ResultSet resultSet) throws SQLException {
        return new Patient(
                resultSet.getString("insurance_policy_number"),
                resultSet.getString("full_name"),
                resultSet.getDate("date_of_birth").toLocalDate(),
                genderMapper.mapGenderFromDatabase(resultSet),
                resultSet.getString("phone_number"),
                resultSet.getString("address"),
                resultSet.getString("password")
        );
    }

    public PatientProfileDto mapPatientToPatientProfileDto(Patient patient) {
        return new PatientProfileDto(patient.getInsurancePolicyNumber(),
                patient.getFullName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getAddress());
    }
}
